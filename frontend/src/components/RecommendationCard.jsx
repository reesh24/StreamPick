import { Sparkles, Clock, Star, Tv, RefreshCw, Home } from 'lucide-react';
import { useState, useEffect } from 'react';

/**
 * RecommendationCard component - Display the recommended movie
 * 
 * Shows movie details, AI reasoning, and match score
 */
export default function RecommendationCard({ recommendation, onTryAnother, onStartOver, loading, error }) {
  
  // State to track which movie is currently featured
  const [featuredIndex, setFeaturedIndex] = useState(0);
  
  // Reset featured index when new recommendations are loaded
  useEffect(() => {
    setFeaturedIndex(0);
  }, [recommendation]);
  
  if (loading) {
    return (
      <div className="text-center space-y-6 animate-fade-in">
        <div className="flex justify-center">
          <div className="w-20 h-20 border-4 border-purple-300 border-t-white rounded-full animate-spin" />
        </div>
        <div className="space-y-2">
          <h3 className="text-2xl font-bold text-white">Finding your perfect match...</h3>
          <p className="text-purple-200">Analyzing your preferences with AI ✨</p>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-center space-y-6 animate-fade-in">
        <div className="p-8 bg-red-500/10 backdrop-blur-lg rounded-2xl border-2 border-red-500/30">
          <p className="text-2xl font-bold text-red-200 mb-4">😕 Oops!</p>
          <p className="text-white mb-2">{error}</p>
          <p className="text-red-200 text-sm">Please try again with different preferences</p>
        </div>
        <div className="flex gap-4 justify-center">
          <button
            onClick={onTryAnother}
            className="px-6 py-3 bg-purple-600 text-white font-semibold rounded-lg 
                     hover:bg-purple-700 transition-colors"
          >
            Try Again
          </button>
          <button
            onClick={onStartOver}
            className="px-6 py-3 bg-white/10 text-white font-semibold rounded-lg 
                     hover:bg-white/20 transition-colors"
          >
            Start Over
          </button>
        </div>
      </div>
    );
  }

  if (!recommendation || !recommendation.recommendations || recommendation.recommendations.length === 0) {
    return null;
  }

  // Get all recommendations
  const allRecommendations = recommendation.recommendations;
  
  // Get featured movie based on state
  const featured = allRecommendations[featuredIndex];
  const { movie, aiReason, matchScore } = featured;
  
  // Get alternatives (all movies except the featured one)
  const alternatives = allRecommendations.filter((_, index) => index !== featuredIndex);
  
  // Handler to swap featured movie
  const handleAlternativeClick = (clickedIndex) => {
    // Find the original index of the clicked alternative
    let originalIndex = 0;
    let count = 0;
    for (let i = 0; i < allRecommendations.length; i++) {
      if (i !== featuredIndex) {
        if (count === clickedIndex) {
          originalIndex = i;
          break;
        }
        count++;
      }
    }
    setFeaturedIndex(originalIndex);
    // Smooth scroll to top of the card
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <div className="space-y-8 animate-fade-in max-w-6xl mx-auto">
      {/* Header */}
      <div className="text-center space-y-2">
        <div className="flex items-center justify-center gap-2 text-yellow-300">
          <Sparkles className="w-6 h-6" />
          <span className="text-sm font-semibold uppercase tracking-wide">Your Perfect Match</span>
          <Sparkles className="w-6 h-6" />
        </div>
      </div>

      {/* Main card */}
      <div className="bg-white/10 backdrop-blur-lg rounded-3xl border-2 border-white/20 overflow-hidden shadow-2xl">
        <div className="grid md:grid-cols-2 gap-8 p-8">
          
          {/* Left side - Movie poster/info */}
          <div className="space-y-6">
            {/* Poster */}
            <div className="relative aspect-[2/3] bg-gradient-to-br from-purple-900/50 to-pink-900/50 rounded-2xl 
                          border-2 border-white/10 overflow-hidden shadow-2xl">
              {movie.imageUrl || movie.image?.url ? (
                <>
                  <img 
                    src={movie.imageUrl || movie.image?.url} 
                    alt={movie.title}
                    className="w-full h-full object-cover"
                    style={{ objectFit: 'cover', objectPosition: 'center' }}
                    onError={(e) => {
                      e.target.style.display = 'none';
                      e.target.parentElement.querySelector('.placeholder').style.display = 'flex';
                    }}
                  />
                  {/* Subtle overlay for better text contrast if needed */}
                  <div className="absolute inset-0 bg-gradient-to-t from-black/20 via-transparent to-transparent pointer-events-none"></div>
                </>
              ) : null}
              <div className="placeholder text-center text-white/40 w-full h-full flex flex-col items-center justify-center absolute inset-0"
                   style={{ display: movie.imageUrl || movie.image?.url ? 'none' : 'flex' }}>
                <Tv className="w-20 h-20 mx-auto mb-4" />
                <p className="text-sm">Movie Poster</p>
              </div>
            </div>

            {/* Platforms */}
            {movie.platforms && movie.platforms.length > 0 && (
              <div>
                <p className="text-purple-300 text-sm mb-2">Available on:</p>
                <div className="flex flex-wrap gap-2">
                  {movie.platforms.map((platform, index) => (
                    <span
                      key={index}
                      className="px-3 py-1 bg-purple-600/30 text-purple-100 rounded-full text-sm border border-purple-400/30"
                    >
                      {platform}
                    </span>
                  ))}
                </div>
              </div>
            )}
          </div>

          {/* Right side - Details */}
          <div className="space-y-6">
            {/* Title */}
            <div>
              <h1 className="text-4xl md:text-5xl font-bold text-white mb-3">
                {movie.title}
              </h1>
              
              {/* Meta info */}
              <div className="flex flex-wrap gap-4 text-purple-200">
                {movie.year && <span>{movie.year}</span>}
                {movie.runtime && (
                  <span className="flex items-center gap-1">
                    <Clock className="w-4 h-4" />
                    {movie.runtime} mins
                  </span>
                )}
                {movie.rating && (
                  <span className="flex items-center gap-1">
                    <Star className="w-4 h-4 fill-yellow-400 text-yellow-400" />
                    {movie.rating}/10
                  </span>
                )}
              </div>
            </div>

            {/* Match score */}
            <div className="p-4 bg-gradient-to-r from-purple-600/30 to-pink-600/30 rounded-xl border border-purple-400/30">
              <div className="flex items-center justify-between">
                <span className="text-purple-200 font-semibold">Match Score</span>
                <span className="text-3xl font-bold text-white">{matchScore}%</span>
              </div>
              <div className="mt-2 h-2 bg-white/10 rounded-full overflow-hidden">
                <div 
                  className="h-full bg-gradient-to-r from-purple-500 to-pink-500 rounded-full transition-all duration-1000"
                  style={{ width: `${matchScore}%` }}
                />
              </div>
            </div>

            {/* Genres */}
            {movie.genre && movie.genre.length > 0 && (
              <div>
                <p className="text-purple-300 text-sm mb-2">Genres:</p>
                <div className="flex flex-wrap gap-2">
                  {movie.genre.map((g, index) => (
                    <span
                      key={index}
                      className="px-3 py-1 bg-white/10 text-white rounded-full text-sm"
                    >
                      {g}
                    </span>
                  ))}
                </div>
              </div>
            )}

            {/* Description */}
            {movie.description && (
              <div>
                <p className="text-purple-100 leading-relaxed">
                  {movie.description}
                </p>
              </div>
            )}

            {/* AI Reason */}
            {aiReason && (
              <div className="p-5 bg-gradient-to-br from-yellow-500/10 to-orange-500/10 rounded-xl border-2 border-yellow-400/30">
                <div className="flex items-start gap-3">
                  <Sparkles className="w-5 h-5 text-yellow-300 flex-shrink-0 mt-1" />
                  <div>
                    <p className="text-yellow-200 font-semibold text-sm mb-1">Why this is perfect for you:</p>
                    <p className="text-white leading-relaxed">
                      {aiReason}
                    </p>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>

      {/* Alternative recommendations */}
      {alternatives && alternatives.length > 0 && (
        <div className="space-y-4">
          <div className="text-center space-y-1">
            <h3 className="text-2xl font-bold text-white">You might also like</h3>
            <p className="text-purple-300 text-sm">👆 Click any movie to view details</p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            {alternatives.map((alt, index) => (
              <div
                key={index}
                onClick={() => handleAlternativeClick(index)}
                className="bg-white/5 backdrop-blur-lg rounded-xl border border-white/10 p-4 
                          hover:bg-white/10 hover:border-white/20 hover:scale-105 hover:shadow-2xl 
                          transition-all duration-300 cursor-pointer group"
              >
                {/* Poster */}
                <div className="relative aspect-[2/3] bg-gradient-to-br from-purple-900/30 to-pink-900/30 rounded-lg mb-3
                              overflow-hidden shadow-lg border border-white/5">
                  {alt.movie.imageUrl || alt.movie.image?.url ? (
                    <>
                      <img 
                        src={alt.movie.imageUrl || alt.movie.image?.url} 
                        alt={alt.movie.title}
                        className="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300"
                        style={{ objectFit: 'cover', objectPosition: 'center' }}
                        onError={(e) => {
                          e.target.style.display = 'none';
                          e.target.parentElement.querySelector('.placeholder-small')?.style.setProperty('display', 'flex');
                        }}
                      />
                      {/* Hover overlay */}
                      <div className="absolute inset-0 bg-black/60 opacity-0 group-hover:opacity-100 
                                    transition-opacity duration-300 flex items-center justify-center z-10">
                        <span className="text-white font-bold text-sm">👁️ View Details</span>
                      </div>
                    </>
                  ) : null}
                  <div className="placeholder-small absolute inset-0 flex items-center justify-center"
                       style={{ display: alt.movie.imageUrl || alt.movie.image?.url ? 'none' : 'flex' }}>
                    <Tv className="w-10 h-10 text-white/30" />
                  </div>
                </div>

                {/* Title */}
                <h4 className="text-lg font-bold text-white mb-2 line-clamp-2">
                  {alt.movie.title}
                </h4>

                {/* Info */}
                <div className="space-y-1 text-sm text-purple-200 mb-3">
                  <div className="flex items-center justify-between">
                    <span>{alt.movie.year}</span>
                    <span className="flex items-center gap-1">
                      <Clock className="w-3 h-3" />
                      {alt.movie.runtime}m
                    </span>
                  </div>
                  <div className="flex items-center gap-1">
                    <Star className="w-3 h-3 fill-yellow-400 text-yellow-400" />
                    <span>{alt.movie.rating}/10</span>
                  </div>
                </div>

                {/* Match score badge */}
                <div className="px-3 py-1 bg-purple-600/30 rounded-full text-center border border-purple-400/30">
                  <span className="text-sm font-semibold text-purple-100">
                    {Math.round(alt.matchScore)}% Match
                  </span>
                </div>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Action buttons */}
      <div className="flex flex-col sm:flex-row gap-4 justify-center">
        <button
          onClick={onTryAnother}
          className="flex items-center justify-center gap-2 px-8 py-4 bg-purple-600 text-white font-bold rounded-xl 
                   hover:bg-purple-700 hover:scale-105 active:scale-95 transition-all duration-300 shadow-lg"
        >
          <RefreshCw className="w-5 h-5" />
          Try Another
        </button>
        <button
          onClick={onStartOver}
          className="flex items-center justify-center gap-2 px-8 py-4 bg-white/10 text-white font-bold rounded-xl 
                   hover:bg-white/20 hover:scale-105 active:scale-95 transition-all duration-300 border-2 border-white/20"
        >
          <Home className="w-5 h-5" />
          Start Over
        </button>
      </div>

      {/* Info footer */}
      <div className="text-center space-y-2">
        <p className="text-purple-300 text-sm">
          ✨ These recommendations were generated using AI-powered content-based filtering
        </p>
        <p className="text-purple-400 text-xs">
          {recommendation.source === 'ml' ? '🤖 ML-Powered' : '⚡ Smart Fallback'} • 
          {' '}{recommendation.totalCandidates} movies analyzed
        </p>
      </div>
    </div>
  );
}

