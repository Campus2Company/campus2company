import path from 'path';
import { fileURLToPath } from 'url';

const __dirname = fileURLToPath(new URL('.', import.meta.url));

const nextConfig = {
  transpilePackages: ['@campus2company/shared'],
  webpack: (config) => {
    config.resolve.alias['react'] = path.resolve(__dirname, 'node_modules/react');
    config.resolve.alias['react-dom'] = path.resolve(__dirname, 'node_modules/react-dom');
    return config;
  },
};

export default nextConfig;