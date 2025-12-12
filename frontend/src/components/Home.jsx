import { Sparkles } from 'lucide-react';

/**
 * Home component - Landing page for StreamPick
 * 
 * Shows the main call-to-action to start the recommendation flow
 */
export default function Home({ onStart, onSubscribe }) {
  return (
    <div className="text-center space-y-8 animate-fade-in relative">
      {/* Floating movie elements - Background decoration */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none opacity-20">
        <div className="absolute top-10 left-10 text-6xl animate-float">🎬</div>
        <div className="absolute top-32 right-16 text-5xl animate-float-delayed">🍿</div>
        <div className="absolute bottom-32 left-20 text-5xl animate-float-slow">🎥</div>
        <div className="absolute top-48 right-32 text-4xl animate-float">🎞️</div>
        <div className="absolute bottom-20 right-12 text-6xl animate-float-delayed">🎭</div>
        <div className="absolute top-20 left-1/3 text-4xl animate-float-slow">📽️</div>
        <div className="absolute bottom-40 right-1/4 text-5xl animate-float">🌟</div>
      </div>

      {/* Logo/Icon */}
      <div className="flex justify-center relative z-10">
        <div className="relative">
          <div className="absolute inset-0 bg-gradient-to-r from-blue-400 to-cyan-400 rounded-full blur-3xl opacity-60 animate-pulse"></div>
          <div className="relative p-8 bg-gradient-to-br from-blue-500/10 to-cyan-500/10 backdrop-blur-lg rounded-full border-2 border-cyan-400/50 shadow-2xl">
            <Sparkles className="w-20 h-20 text-amber-400" />
          </div>
        </div>
      </div>

      {/* Title */}
      <div className="space-y-4">
        <h1 className="text-8xl font-black bg-gradient-to-r from-cyan-300 via-blue-400 to-cyan-300 bg-clip-text text-transparent drop-shadow-2xl">
          StreamPick
        </h1>
        <p className="text-3xl text-slate-200 font-light tracking-wide">
          End the scroll. Start watching. 🎬
        </p>
      </div>

      {/* Tagline */}
      <p className="text-2xl text-slate-300 max-w-2xl mx-auto leading-relaxed">
        Find your perfect movie in <span className="text-amber-400 text-3xl">3 clicks</span>.
      </p>

      {/* CTA Buttons */}
      <div className="pt-8 space-y-4">
        <button
          onClick={onStart}
          className="group relative px-12 py-6 bg-gradient-to-r from-cyan-500 to-blue-600 text-white font-bold text-2xl rounded-xl 
                     hover:scale-105 active:scale-95 transition-all duration-300 
                     shadow-2xl hover:shadow-cyan-500/50 border border-cyan-300/20"
        >
          <span className="flex items-center gap-3">
            <Sparkles className="w-6 h-6 group-hover:rotate-12 transition-transform" />
            Find My Perfect Watch
            <Sparkles className="w-6 h-6 group-hover:-rotate-12 transition-transform" />
          </span>
        </button>
        
        <div>
          <button
            onClick={onSubscribe}
            className="group px-8 py-4 bg-slate-800/80 backdrop-blur-sm text-white font-semibold text-lg rounded-xl 
                       hover:scale-105 active:scale-95 transition-all duration-300 
                       shadow-lg hover:shadow-amber-500/30 border border-amber-400/40 hover:border-amber-400/80"
          >
            <span className="flex items-center gap-2">
              📧 Get Recommendations via Email
            </span>
          </button>
        </div>
      </div>

      {/* Feature badges */}
      <div className="flex justify-center gap-4 pt-8 text-sm text-white relative z-10">
        <div className="flex items-center gap-2 px-5 py-3 bg-white/5 backdrop-blur-sm rounded-full border border-cyan-400/30 hover:border-cyan-400/60 hover:scale-105 transition-all">
          <span className="text-lg">⚡</span>
          <span className="font-medium">Instant Results</span>
        </div>
        <div className="flex items-center gap-2 px-5 py-3 bg-white/5 backdrop-blur-sm rounded-full border border-blue-400/30 hover:border-blue-400/60 hover:scale-105 transition-all">
          <span className="text-lg">🎯</span>
          <span className="font-medium">Smart Matching</span>
        </div>
        <div className="flex items-center gap-2 px-5 py-3 bg-white/5 backdrop-blur-sm rounded-full border border-amber-400/30 hover:border-amber-400/60 hover:scale-105 transition-all">
          <span className="text-lg">✨</span>
          <span className="font-medium">No Sign-Up</span>
        </div>
      </div>
      
      {/* Movie poster grid background */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none opacity-5">
        <div className="grid grid-cols-6 gap-4 absolute inset-0 transform -rotate-12 scale-150">
          {[...Array(30)].map((_, i) => (
            <div key={i} className="aspect-[2/3] bg-gradient-to-br from-white/10 to-white/5 rounded-lg"></div>
          ))}
        </div>
      </div>
    </div>
  );
}

