"""
Mood mapper to handle UI-friendly labels and map them to backend mood tags
"""

class MoodMapper:
    """Maps user-friendly mood labels to backend mood tags"""
    
    # Define mappings: UI label -> backend tag
    MOOD_ALIASES = {
        # Direct mappings (exact match)
        "cozy": "cozy",
        "thrilling": "thrilling",
        "laugh": "laugh",
        "deep": "deep",
        "escape": "escape",
        "chill": "chill",
        
        # UI-friendly aliases
        "cozy & warm": "cozy",
        "cozy and warm": "cozy",
        "warm": "cozy",
        
        "edge of seat": "thrilling",
        "edge-of-seat": "thrilling",
        "thriller": "thrilling",
        "suspense": "thrilling",
        "intense": "thrilling",
        
        "need laughs": "laugh",
        "need-laughs": "laugh",
        "funny": "laugh",
        "comedy": "laugh",
        "humor": "laugh",
        
        "make me think": "deep",
        "make-me-think": "deep",
        "thoughtful": "deep",
        "intellectual": "deep",
        "profound": "deep",
        
        "pure escapism": "escape",
        "pure-escapism": "escape",
        "escapism": "escape",
        "adventure": "escape",
        
        "background vibe": "chill",
        "background-vibe": "chill",
        "background": "chill",
        "relaxing": "chill",
        "mellow": "chill",
    }
    
    @classmethod
    def normalize_mood(cls, mood_input: str) -> str:
        """
        Normalize mood input to backend mood tag
        
        Args:
            mood_input: User's mood input (e.g., "Edge of Seat", "THRILLING", "laugh")
            
        Returns:
            Normalized backend mood tag (e.g., "thrilling", "laugh")
            
        Raises:
            ValueError: If mood is not recognized
        """
        # Normalize: lowercase, strip whitespace
        normalized = mood_input.lower().strip()
        
        # Try direct lookup
        if normalized in cls.MOOD_ALIASES:
            return cls.MOOD_ALIASES[normalized]
        
        # Not found
        raise ValueError(
            f"Mood '{mood_input}' not recognized. "
            f"Valid moods: {cls.get_ui_friendly_moods()}"
        )
    
    @classmethod
    def get_ui_friendly_moods(cls) -> list:
        """Get list of UI-friendly mood labels"""
        return [
            "Cozy & Warm",
            "Edge of Seat",
            "Need Laughs",
            "Make Me Think",
            "Pure Escapism",
            "Background Vibe"
        ]
    
    @classmethod
    def get_backend_moods(cls) -> list:
        """Get list of backend mood tags"""
        return ["cozy", "thrilling", "laugh", "deep", "escape", "chill"]
    
    @classmethod
    def get_all_valid_inputs(cls) -> list:
        """Get all valid mood inputs (for documentation)"""
        return sorted(list(cls.MOOD_ALIASES.keys()))

