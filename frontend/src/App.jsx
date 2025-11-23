import { useState } from 'react';
import Home from './components/Home';
import MoodSelector from './components/MoodSelector';
import TimeSelector from './components/TimeSelector';
import RecommendationCard from './components/RecommendationCard';
import recommendationService from './services/recommendationService';

/**
 * Main App component - Orchestrates the 3-step flow
 * 
 * Flow: Home → Mood → Time → Result
 */
function App() {
  // State management
  const [step, setStep] = useState('home'); // 'home' | 'mood' | 'time' | 'result'
  const [mood, setMood] = useState(null);
  const [timeAvailable, setTimeAvailable] = useState(null);
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  /**
   * Fetch recommendations from backend (returns 5 movies)
   */
  const fetchRecommendation = async (selectedMood, selectedTime) => {
    setLoading(true);
    setError(null);
    setStep('result');

    try {
      const response = await recommendationService.getRecommendation({
        mood: selectedMood,
        timeAvailable: selectedTime
      });

      // Backend now returns { recommendations: [...], totalCandidates, source }
      setRecommendation(response.data);
      console.log('Recommendations received:', response.data);
      console.log(`Got ${response.data.recommendations?.length || 0} movies from ${response.data.source}`);
      
    } catch (err) {
      console.error('Error fetching recommendation:', err);
      setError(
        err.response?.data?.message || 
        'Unable to get recommendations. Please try again.'
      );
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle mood selection
   */
  const handleMoodSelect = (selectedMood) => {
    setMood(selectedMood);
    setStep('time');
  };

  /**
   * Handle time selection and trigger recommendation
   */
  const handleTimeSelect = (selectedTime) => {
    setTimeAvailable(selectedTime);
    fetchRecommendation(mood, selectedTime);
  };

  /**
   * Try another recommendation with same preferences
   */
  const handleTryAnother = () => {
    if (mood && timeAvailable) {
      fetchRecommendation(mood, timeAvailable);
    }
  };

  /**
   * Reset to home screen
   */
  const handleStartOver = () => {
    setStep('home');
    setMood(null);
    setTimeAvailable(null);
    setRecommendation(null);
    setError(null);
  };

  /**
   * Go back one step
   */
  const handleBack = () => {
    if (step === 'mood') {
      setStep('home');
    } else if (step === 'time') {
      setStep('mood');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4 sm:p-6 lg:p-8">
      <div className="w-full max-w-7xl">
        {/* Conditional rendering based on current step */}
        
        {step === 'home' && (
          <Home onStart={() => setStep('mood')} />
        )}

        {step === 'mood' && (
          <MoodSelector 
            onMoodSelect={handleMoodSelect}
            onBack={handleBack}
          />
        )}

        {step === 'time' && (
          <TimeSelector 
            onTimeSelect={handleTimeSelect}
            onBack={handleBack}
          />
        )}

        {step === 'result' && (
          <RecommendationCard 
            recommendation={recommendation}
            loading={loading}
            error={error}
            onTryAnother={handleTryAnother}
            onStartOver={handleStartOver}
          />
        )}
      </div>

      {/* Footer */}
      <footer className="fixed bottom-4 left-0 right-0 text-center">
        <p className="text-purple-300 text-sm">
          Made with ❤️ using Contentstack + Python ML + Spring Boot + React
        </p>
      </footer>
    </div>
  );
}

export default App;
