import React, { useState } from 'react';
import { ArrowLeft } from 'lucide-react';
import api from '../services/api';

const SubscriptionForm = ({ onBack }) => {
  const [formData, setFormData] = useState({
    email: '',
    name: '',
    preferredMoods: []
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState({ text: '', type: '' });
  const [showConfetti, setShowConfetti] = useState(false);

  const moodOptions = [
    { emoji: '☕', label: 'Cozy & Warm', subtitle: 'Feel-good comfort', color: 'from-amber-400 to-orange-500' },
    { emoji: '🎢', label: 'Edge of Seat', subtitle: 'Heart-pounding action', color: 'from-red-500 to-pink-600' },
    { emoji: '😂', label: 'Need Laughs', subtitle: 'Comedy gold', color: 'from-yellow-400 to-amber-500' },
    { emoji: '🧠', label: 'Make Me Think', subtitle: 'Mind-bending stories', color: 'from-purple-500 to-indigo-600' },
    { emoji: '🚀', label: 'Pure Escapism', subtitle: 'Transport me away', color: 'from-blue-500 to-cyan-500' },
    { emoji: '🌊', label: 'Background Vibe', subtitle: 'Relaxed watching', color: 'from-teal-400 to-blue-500' }
  ];

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleMoodToggle = (mood) => {
    setFormData(prev => ({
      ...prev,
      preferredMoods: prev.preferredMoods.includes(mood)
        ? prev.preferredMoods.filter(m => m !== mood)
        : [...prev.preferredMoods, mood]
    }));
  };

  const handleSubscribe = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage({ text: '', type: '' });

    try {
      // Call your Java backend
      const response = await api.post('/api/subscribers/add', {
        name: formData.name,
        email: formData.email,
        preferredMoods: formData.preferredMoods
      });

      const data = response.data;

      if (response.status === 200 && data.success) {
        setMessage({ 
          text: data.message || '🎉 Yay! You\'re now subscribed to movie magic!', 
          type: 'success' 
        });
        setShowConfetti(true);
        setTimeout(() => setShowConfetti(false), 3000);
        
        // Reset form
        setFormData({ email: '', name: '', preferredMoods: [] });
      } else {
        setMessage({ 
          text: data.message || 'Failed to subscribe. Please try again.', 
          type: 'error' 
        });
      }
    } catch (error) {
      console.error('Subscription error:', error);
      setMessage({ 
        text: '😞 Oops! Something went wrong. Please try again.', 
        type: 'error' 
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen p-4 bg-gradient-to-br from-slate-900 via-indigo-900 to-slate-800">
      {/* Confetti Animation */}
      {showConfetti && (
        <div className="fixed inset-0 pointer-events-none z-50">
          {[...Array(50)].map((_, i) => (
            <div
              key={i}
              className="absolute animate-ping"
              style={{
                left: `${Math.random() * 100}%`,
                top: `${Math.random() * 100}%`,
                animationDelay: `${Math.random() * 2}s`,
                animationDuration: '2s'
              }}
            >
              🎉
            </div>
          ))}
        </div>
      )}

      <div className="max-w-6xl mx-auto py-8 space-y-8">
        {/* Back button - outside and above the form */}
        {onBack && (
          <button
            onClick={onBack}
            className="flex items-center gap-2 px-4 py-2 bg-white/10 backdrop-blur-sm text-white rounded-lg hover:bg-white/20 hover:scale-105 transition-all border border-white/20"
          >
            <ArrowLeft className="w-5 h-5" />
            <span className="font-semibold">Back</span>
          </button>
        )}

        {/* Form container */}
        <div className="max-w-md mx-auto animate-fade-in">
          {/* Header */}
          <div className="text-center mb-8 animate-bounce-slow">
          <div className="text-7xl mb-4">🎬</div>
          <h1 className="text-4xl font-bold bg-gradient-to-r from-rose-400 via-orange-400 to-rose-400 bg-clip-text text-transparent mb-2">
            StreamPick
          </h1>
          <p className="text-slate-300 text-lg">
            Get movie magic in your inbox! ✨
          </p>
        </div>

        {/* Form Card */}
        <div className="bg-white/10 backdrop-blur-lg rounded-3xl shadow-2xl p-8 border-2 border-white/20 transform hover:scale-105 transition-transform duration-300">
          
          {/* Message */}
          {message.text && (
            <div className={`mb-6 p-4 rounded-2xl animate-slide-down ${
              message.type === 'success' 
                ? 'bg-green-100 text-green-800 border-2 border-green-300' 
                : 'bg-red-100 text-red-800 border-2 border-red-300'
            }`}>
              <p className="font-medium text-center">{message.text}</p>
            </div>
          )}

          <form onSubmit={handleSubscribe} className="space-y-6">
            {/* Name Input */}
            <div className="group">
              <label className="flex items-center gap-2 text-sm font-semibold text-white mb-2">
                <span className="text-2xl">👤</span>
                <span>Your Name</span>
              </label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                required
                placeholder="John Doe"
                className="w-full px-4 py-3 rounded-2xl border-2 border-rose-300/30 focus:border-rose-400 focus:ring-4 focus:ring-rose-400/20 transition-all outline-none bg-white/10 text-white placeholder-slate-400"
              />
            </div>

            {/* Email Input */}
            <div className="group">
              <label className="flex items-center gap-2 text-sm font-semibold text-white mb-2">
                <span className="text-2xl">📧</span>
                <span>Email Address</span>
              </label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleInputChange}
                required
                placeholder="you@awesome.com"
                className="w-full px-4 py-3 rounded-2xl border-2 border-rose-300/30 focus:border-rose-400 focus:ring-4 focus:ring-rose-400/20 transition-all outline-none bg-white/10 text-white placeholder-slate-400"
              />
            </div>

            {/* Mood Selector */}
            <div>
              <label className="flex items-center gap-2 text-sm font-semibold text-white mb-3">
                <span className="text-2xl">🎭</span>
                <span>Pick Your Vibes</span>
                <span className="text-xs text-slate-400 font-normal">(optional)</span>
              </label>
              <div className="grid grid-cols-2 gap-3">
                {moodOptions.map(mood => (
                  <button
                    key={mood.label}
                    type="button"
                    onClick={() => handleMoodToggle(mood.label)}
                    className={`group relative p-4 rounded-2xl text-left transition-all transform hover:scale-105 active:scale-95 ${
                      formData.preferredMoods.includes(mood.label)
                        ? `bg-gradient-to-br ${mood.color} text-white shadow-xl border-2 border-white`
                        : 'bg-white/5 text-white hover:bg-white/10 border-2 border-white/10'
                    }`}
                  >
                    <div className="text-3xl mb-2">{mood.emoji}</div>
                    <div className="font-bold text-sm">{mood.label}</div>
                    <div className={`text-xs mt-1 ${
                      formData.preferredMoods.includes(mood.label) 
                        ? 'text-white/90' 
                        : 'text-slate-400'
                    }`}>
                      {mood.subtitle}
                    </div>
                  </button>
                ))}
              </div>
              {formData.preferredMoods.length > 0 && (
                <p className="text-xs text-rose-300 mt-2 animate-fade-in">
                  ✨ {formData.preferredMoods.length} mood{formData.preferredMoods.length > 1 ? 's' : ''} selected
                </p>
              )}
            </div>

            {/* Submit Button */}
            <button
              type="submit"
              disabled={loading}
              className={`w-full py-4 rounded-2xl font-bold text-lg transition-all transform ${
                loading
                  ? 'bg-slate-600 cursor-not-allowed'
                  : 'bg-gradient-to-r from-rose-500 to-orange-500 hover:from-rose-600 hover:to-orange-600 text-white shadow-lg hover:shadow-xl hover:scale-105 active:scale-95'
              }`}
            >
              {loading ? (
                <span className="flex items-center justify-center gap-2">
                  <svg className="animate-spin h-5 w-5" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z" />
                  </svg>
                  Subscribing...
                </span>
              ) : (
                <span className="flex items-center justify-center gap-2">
                  <span>🎬</span>
                  Subscribe to Movie Magic
                  <span>✨</span>
                </span>
              )}
            </button>
          </form>

          {/* Features */}
          <div className="mt-8 pt-6 border-t-2 border-white/10">
            <p className="text-xs text-slate-400 text-center mb-3 font-semibold">
              What you'll get:
            </p>
            <div className="space-y-2">
              <div className="flex items-center gap-2 text-sm text-white">
                <span className="text-lg">🎥</span>
                <span>New movie alerts instantly</span>
              </div>
              <div className="flex items-center gap-2 text-sm text-white">
                <span className="text-lg">🎯</span>
                <span>Personalized by your mood</span>
              </div>
              <div className="flex items-center gap-2 text-sm text-white">
                <span className="text-lg">🎨</span>
                <span>Beautiful email designs</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

      {/* Custom Animations */}
      <style jsx>{`
        @keyframes bounce-slow {
          0%, 100% { transform: translateY(0); }
          50% { transform: translateY(-10px); }
        }
        @keyframes slide-down {
          from { transform: translateY(-20px); opacity: 0; }
          to { transform: translateY(0); opacity: 1; }
        }
        @keyframes fade-in {
          from { opacity: 0; }
          to { opacity: 1; }
        }
        .animate-bounce-slow {
          animation: bounce-slow 3s infinite;
        }
        .animate-slide-down {
          animation: slide-down 0.3s ease-out;
        }
        .animate-fade-in {
          animation: fade-in 0.5s ease-out;
        }
      `}</style>
    </div>
  );
};

export default SubscriptionForm;

