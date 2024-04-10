package ru.kpfu.itis.gimaletdinova.quizapp.util.enums

enum class Category(val id: Int, private val displayName: String) {
    GENERAL_KNOWLEDGE(9, "General Knowledge"),
    BOOKS(10, "Books"),
    FILMS(11, "Films"),
    MUSIC(12, "Music"),
    MUSICALS_AND_THEATRES(13, "Musicals & Theatres"),
    TELEVISION(14, "Television"),
    VIDEO_GAMES(15, "Video Games"),
    BOARD_GAMES(16, "Board Games"),
    SCIENCE_AND_NATURE(17, "Science & Nature"),
    COMPUTERS(18, "Computers"),
    MATHEMATICS(19, "Mathematics"),
    MYTHOLOGY(20, "Mythology"),
    SPORTS(21, "Sports"),
    GEOGRAPHY(22, "Geography"),
    HISTORY(23, "History"),
    POLITICS(24, "Politics"),
    ART(25, "Art"),
    CELEBRITIES(26, "Celebrities"),
    ANIMALS(27, "Animals"),
    VEHICLES(28, "Vehicles"),
    COMICS(29, "Comics"),
    GADGETS(30, "Gadgets"),
    ANIME_AND_MANGA(31, "Anime & Manga"),
    CARTOON_AND_ANIMATIONS(32, "Cartoon & Animations");

    override fun toString(): String {
        return displayName
    }

}