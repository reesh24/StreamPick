import { Sparkles } from 'lucide-react';

/**
 * Home component - Landing page for StreamPick
 * 
 * Shows the main call-to-action to start the recommendation flow
 */
export default function Home({ onStart }) {
  return (
    <div className="text-center space-y-8 animate-fade-in">
      {/* Logo/Icon */}
      <div className="flex justify-center">
        <div className="p-6 bg-white/10 backdrop-blur-lg rounded-full border border-white/20">
          <Sparkles className="w-16 h-16 text-yellow-300" />
        </div>
      </div>

      {/* Title */}
      <div className="space-y-4">
        <h1 className="text-7xl font-bold bg-gradient-to-r from-white via-purple-200 to-pink-200 bg-clip-text text-transparent">
          StreamPick
        </h1>
        <p className="text-3xl text-purple-100 font-light">
          End the scroll. Start watching.
        </p>
      </div>

      {/* Tagline */}
      <p className="text-xl text-purple-200 max-w-2xl mx-auto leading-relaxed">
        Find your perfect movie in <span className="font-bold text-white">3 clicks</span>.
        <br />
        Powered by AI + contextual intelligence.
      </p>

      {/* CTA Button */}
      <div className="pt-8">
        <button
          onClick={onStart}
          className="group relative px-12 py-5 bg-white text-purple-900 font-bold text-xl rounded-2xl 
                     hover:scale-105 active:scale-95 transition-all duration-300 
                     shadow-2xl hover:shadow-purple-500/50"
        >
          <span className="flex items-center gap-3">
            <Sparkles className="w-6 h-6 group-hover:rotate-12 transition-transform" />
            Find My Perfect Watch
            <Sparkles className="w-6 h-6 group-hover:-rotate-12 transition-transform" />
          </span>
        </button>
      </div>

      {/* Feature badges */}
      <div className="flex justify-center gap-4 pt-8 text-sm text-purple-300">
        <div className="flex items-center gap-2 px-4 py-2 bg-white/5 rounded-full border border-white/10">
          <span>⚡</span>
          <span>Instant Results</span>
        </div>
        <div className="flex items-center gap-2 px-4 py-2 bg-white/5 rounded-full border border-white/10">
          <span>🎯</span>
          <span>Smart Matching</span>
        </div>
        <div className="flex items-center gap-2 px-4 py-2 bg-white/5 rounded-full border border-white/10">
          <span>✨</span>
          <span>No Sign-Up</span>
        </div>
      </div>
    </div>
  );
}

