import { Clock, ArrowLeft } from 'lucide-react';

/**
 * TimeSelector component - Let user choose how much time they have
 * 
 * Shows 3 time options: quick, standard, or marathon
 */
export default function TimeSelector({ onTimeSelect, onBack }) {
  const timeOptions = [
    {
      minutes: 30,
      icon: '⚡',
      label: 'Quick Watch',
      description: '30 minutes',
      subtitle: 'Short episode or clip'
    },
    {
      minutes: 90,
      icon: '🎬',
      label: 'Movie Night',
      description: '90 minutes',
      subtitle: 'Standard movie length'
    },
    {
      minutes: 180,
      icon: '🍿',
      label: 'Binge Mode',
      description: '3+ hours',
      subtitle: 'Epic marathon session'
    }
  ];

  return (
    <div className="space-y-8 animate-fade-in">
      {/* Back button */}
      {onBack && (
        <button
          onClick={onBack}
          className="flex items-center gap-2 text-purple-200 hover:text-white transition-colors"
        >
          <ArrowLeft className="w-5 h-5" />
          Back
        </button>
      )}

      {/* Header */}
      <div className="text-center space-y-4">
        <div className="flex justify-center">
          <Clock className="w-16 h-16 text-purple-300" />
        </div>
        <h2 className="text-5xl font-bold text-white">
          How much time do you have?
        </h2>
        <p className="text-xl text-purple-200">
          We'll find something that fits perfectly
        </p>
      </div>

      {/* Time options */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-5xl mx-auto">
        {timeOptions.map((option) => (
          <button
            key={option.minutes}
            onClick={() => onTimeSelect(option.minutes)}
            className="group relative p-10 bg-white/10 backdrop-blur-lg rounded-2xl border-2 border-white/20 
                       hover:border-white/40 hover:bg-white/15 hover:scale-105 
                       active:scale-95 transition-all duration-300 text-center space-y-4"
          >
            {/* Icon */}
            <div className="text-6xl group-hover:scale-110 transition-transform">
              {option.icon}
            </div>

            {/* Label */}
            <div className="space-y-2">
              <h3 className="text-3xl font-bold text-white">
                {option.label}
              </h3>
              <p className="text-2xl text-purple-200 font-semibold">
                {option.description}
              </p>
              <p className="text-sm text-purple-300">
                {option.subtitle}
              </p>
            </div>

            {/* Hover indicator */}
            <div className="absolute inset-0 rounded-2xl bg-gradient-to-r from-purple-500/0 to-pink-500/0 
                          group-hover:from-purple-500/10 group-hover:to-pink-500/10 transition-all duration-300" />
          </button>
        ))}
      </div>

      {/* Helper text */}
      <p className="text-center text-purple-300 text-sm">
        💡 Don't worry, we'll consider your actual available time in the recommendation
      </p>
    </div>
  );
}

