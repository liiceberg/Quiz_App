package ru.kpfu.itis.gimaletdinova.quizapp.util.enums

import com.google.gson.annotations.SerializedName

enum class Category(private val displayName: String) {
    @SerializedName("General Knowledge")
    GENERAL_KNOWLEDGE("General Knowledge"),

    @SerializedName("Entertainment: Books")
    BOOKS("Books"),

    @SerializedName("Entertainment: Film")
    FILMS("Films"),

    @SerializedName("Entertainment: Music")
    MUSIC("Music"),

    @SerializedName("Entertainment: Musicals & Theatres")
    MUSICALS_AND_THEATRES("Musicals & Theatres"),

    @SerializedName("Entertainment: Television")
    TELEVISION("Television"),

    @SerializedName("Entertainment: Video Games")
    VIDEO_GAMES("Video Games"),

    @SerializedName("Entertainment: Board Games")
    BOARD_GAMES("Board Games"),

    @SerializedName("Science &amp; Nature")
    SCIENCE_AND_NATURE("Science & Nature"),

    @SerializedName("Science: Computers")
    COMPUTERS("Computers"),

    @SerializedName("Science: Mathematics")
    MATHEMATICS("Mathematics"),

    @SerializedName("Mythology")
    MYTHOLOGY("Mythology"),

    @SerializedName("Sports")
    SPORTS("Sports"),

    @SerializedName("Geography")
    GEOGRAPHY("Geography"),

    @SerializedName("History")
    HISTORY("History"),

    @SerializedName("Politics")
    POLITICS("Politics"),

    @SerializedName("Art")
    ART("Art"),

    @SerializedName("Celebrities")
    CELEBRITIES("Celebrities"),

    @SerializedName("Animals")
    ANIMALS("Animals"),

    @SerializedName("Vehicles")
    VEHICLES("Vehicles"),

    @SerializedName("Entertainment: Comics")
    COMICS("Comics"),

    @SerializedName("Science: Gadgets")
    GADGETS("Gadgets"),

    @SerializedName("Entertainment: Japanese Anime & Manga")
    ANIME_AND_MANGA("Anime & Manga"),

    @SerializedName("Entertainment: Cartoon & Animations")
    CARTOON_AND_ANIMATIONS("Cartoon & Animations"),

    OTHER("Other");

    fun getName(): String {
        return displayName
    }
}