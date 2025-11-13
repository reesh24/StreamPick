import api from './api';

/**
 * Service for movie recommendation API calls
 */
const recommendationService = {
  /**
   * Get movie recommendation based on mood and available time
   * @param {Object} data - Request data
   * @param {string} data.mood - User's selected mood
   * @param {number} data.timeAvailable - Available time in minutes
   * @returns {Promise} Axios promise with recommendation response
   */
  getRecommendation: (data) => {
    return api.post('/api/recommendations', data);
  },

  /**
   * Get all available movies
   * @returns {Promise} Axios promise with movies array
   */
  getAllMovies: () => {
    return api.get('/api/movies');
  },

  /**
   * Get movies by mood
   * @param {string} mood - Mood to filter by
   * @returns {Promise} Axios promise with movies array
   */
  getMoviesByMood: (mood) => {
    return api.get(`/api/movies/mood/${mood}`);
  },
};

export default recommendationService;

