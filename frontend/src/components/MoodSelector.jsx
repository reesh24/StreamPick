import { ArrowLeft } from 'lucide-react';

/**
 * MoodSelector component - Let user choose their current vibe
 * 
 * Shows 6 mood options with emojis
 */
export default function MoodSelector({ onMoodSelect, onBack }) {
  const moods = [
    {
      id: 'cozy',
      emoji: '☕',
      label: 'Cozy & Warm',
      description: 'Feel-good comfort'
    },
    {
      id: 'thrilling',
      emoji: '🎢',
      label: 'Edge of Seat',
      description: 'Heart-pounding action'
    },
    {
      id: 'laugh',
      emoji: '😂',
      label: 'Need Laughs',
      description: 'Comedy gold'
    },
    {
      id: 'deep',
      emoji: '🧠',
      label: 'Make Me Think',
      description: 'Mind-bending stories'
    },
    {
      id: 'escape',
      emoji: '🚀',
      label: 'Pure Escapism',
      description: 'Transport me away'
    },
    {
      id: 'chill',
      emoji: '🌊',
      label: 'Background Vibe',
      description: 'Relaxed watching'
    }
  ];

  return (
    <div className="space-y-8 animate-fade-in relative">
      {/* Decorative movie emojis */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none opacity-10">
        <div className="absolute top-20 right-10 text-5xl animate-float">🎭</div>
        <div className="absolute bottom-20 left-10 text-5xl animate-float-delayed">🎪</div>
        <div className="absolute top-40 left-20 text-4xl animate-float-slow">🎨</div>
      </div>

      {/* Back button */}
      {onBack && (
        <button
          onClick={onBack}
          className="flex items-center gap-2 px-4 py-2 bg-white/10 backdrop-blur-sm text-white rounded-lg hover:bg-white/20 hover:scale-105 transition-all border border-white/20"
        >
          <ArrowLeft className="w-5 h-5" />
          <span className="font-semibold">Back</span>
        </button>
      )}

      {/* Header */}
      <div className="text-center space-y-4">
        <h2 className="text-5xl font-bold text-white">
          What's your vibe?
        </h2>
        <p className="text-xl text-slate-300">
          Pick the mood that matches how you're feeling
        </p>
      </div>

      {/* Mood grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 max-w-5xl mx-auto">
        {moods.map((mood) => (
          <button
            key={mood.id}
            onClick={() => onMoodSelect(mood.id)}
            className="group relative p-8 bg-white/10 backdrop-blur-lg rounded-2xl border-2 border-white/20 
                       hover:border-white/40 hover:bg-white/15 hover:scale-105 
                       active:scale-95 transition-all duration-300 text-center"
          >
            {/* Emoji */}
            <div className="text-6xl mb-4 group-hover:scale-110 transition-transform">
              {mood.emoji}
            </div>

            {/* Label */}
            <h3 className="text-2xl font-bold text-white mb-2">
              {mood.label}
            </h3>

            {/* Description */}
            <p className="text-slate-300 text-sm">
              {mood.description}
            </p>

            {/* Hover indicator */}
            <div className="absolute inset-0 rounded-2xl bg-gradient-to-r from-rose-500/0 to-orange-500/0 
                          group-hover:from-rose-500/10 group-hover:to-orange-500/10 transition-all duration-300" />
          </button>
        ))}
      </div>
    </div>
  );
}

