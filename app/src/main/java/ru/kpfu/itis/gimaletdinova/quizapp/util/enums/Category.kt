package ru.kpfu.itis.gimaletdinova.quizapp.util.enums

enum class Category(private val displayName: String) {
    GENERAL_KNOWLEDGE("General Knowledge"),
    BOOKS("Books"),
    FILMS("Films"),
    MUSIC("Music"),
    MUSICALS_AND_THEATRES("Musicals & Theatres"),
    TELEVISION("Television"),
    VIDEO_GAMES("Video Games"),
    BOARD_GAMES("Board Games"),
    SCIENCE_AND_NATURE("Science & Nature"),
    COMPUTERS("Computers"),
    MATHEMATICS("Mathematics"),
    MYTHOLOGY("Mythology"),
    SPORTS("Sports"),
    GEOGRAPHY("Geography"),
    HISTORY("History"),
    POLITICS("Politics"),
    ART("Art"),
    CELEBRITIES("Celebrities"),
    ANIMALS("Animals"),
    VEHICLES("Vehicles"),
    COMICS("Comics"),
    GADGETS("Gadgets"),
    ANIME_AND_MANGA("Anime & Manga"),
    CARTOON_AND_ANIMATIONS("Cartoon & Animations"),
    OTHER("Other");

    fun getName() : String {
        return displayName
    }
}