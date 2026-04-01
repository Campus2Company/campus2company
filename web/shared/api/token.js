import { getAccessToken, setAccessToken } from '../context/AuthContext';

const API_BASE = (process.env.NEXT_PUBLIC_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '');

let isRefreshing = false;
let waitingQueue = [];

async function doRefresh() {
    const res = await fetch(`${API_BASE}/auth/refresh`, {
        method: 'POST',
        credentials: 'include',
    });

    if (!res.ok) {
        setAccessToken(null);
        throw new Error('Session expired');
    }

    const data = await res.json();
    setAccessToken(data.accessToken);
    return data.accessToken;
}

export async function apiFetch(url, options = {}) {
    const token = getAccessToken();

    const headers = {
        'Content-Type': 'application/json',
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
        ...options.headers,
    };

    const res = await fetch(url, {
        ...options,
        headers,
        credentials: 'include',
    });

    if (res.status !== 401) return res;

    if (isRefreshing) {
        return new Promise((resolve, reject) => {
            waitingQueue.push({ resolve, reject, url, options });
        });
    }

    isRefreshing = true;

    try {
        await doRefresh();

        waitingQueue.forEach(({ resolve, reject, url: u, options: o }) => {
            apiFetch(u, o).then(resolve).catch(reject);
        });
        waitingQueue = [];

        return apiFetch(url, options);
    } catch (err) {
        waitingQueue.forEach(({ reject }) => reject(err));
        waitingQueue = [];
        window.location.href = 'http://localhost:3000/';
        throw err;
    } finally {
        isRefreshing = false;
    }
}