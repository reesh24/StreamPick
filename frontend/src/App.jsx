import { useState } from 'react';

function App() {
  const [step, setStep] = useState('home');

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <div className="max-w-4xl w-full">
        <div className="text-center text-white">
          <h1 className="text-6xl font-bold mb-4 bg-gradient-to-r from-white to-pink-200 bg-clip-text text-transparent">
            StreamPick
          </h1>
          <p className="text-2xl mb-8 text-purple-100">
            End the scroll. Start watching.
          </p>
          
          {/* Placeholder for components */}
          <div className="card">
            <p className="text-white text-lg">
              🎬 StreamPick is ready!
            </p>
            <p className="text-purple-200 mt-4">
              Components will be added next.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;

