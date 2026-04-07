export async function getServerSideProps() {
  return {
    redirect: {
      destination: '/listings',
      permanent: false
    }
  };
}

export default function Home() {
  return null;
}
