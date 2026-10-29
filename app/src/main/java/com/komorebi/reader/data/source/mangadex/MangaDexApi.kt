package com.komorebi.reader.data.source.mangadex

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * MangaDex API v5 - Minimal implementation for browsing
 * Docs: https://api.mangadex.org/docs.html
 */

interface MangaDexApi {
    @GET("manga")
    suspend fun searchManga(
        @Query("title") title: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("includes[]") includes: List<String> = listOf("cover_art", "author", "artist"),
        @Query("contentRating[]") contentRating: List<String> = listOf("safe", "suggestive", "erotica", "pornographic")
    ): Response<MangaSearchResponse>

    @GET("manga/{id}")
    suspend fun getManga(
        @Path("id") id: String,
        @Query("includes[]") includes: List<String> = listOf("cover_art", "author", "artist")
    ): Response<MangaResponse>

    @GET("manga/{id}/feed")
    suspend fun getMangaFeed(
        @Path("id") id: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0,
        @Query("translatedLanguage[]") translatedLanguage: List<String> = listOf("fr", "en"),
        @Query("order[chapter]") order: String = "asc",
        @Query("includes[]") includes: List<String> = listOf("scanlation_group")
    ): Response<ChapterFeedResponse>

    @GET("cover/{coverId}")
    suspend fun getCover(@Path("coverId") coverId: String): Response<CoverResponse>

    @GET("at-home/server/{chapterId}")
    suspend fun getChapterPages(@Path("chapterId") chapterId: String): Response<AtHomeResponse>

    @GET("author/{id}")
    suspend fun getAuthor(@Path("id") id: String): Response<AuthorResponse>

    @GET("manga/random")
    suspend fun getRandomManga(): Response<MangaResponse>
}

// ===== RESPONSE MODELS =====

@Serializable
data class MangaSearchResponse(
    val result: String,
    val response: String,
    val data: List<Manga>,
    val limit: Int,
    val offset: Int,
    val total: Int
)

@Serializable
data class MangaResponse(
    val result: String,
    val data: Manga
)

@Serializable
data class ChapterFeedResponse(
    val result: String,
    val response: String,
    val data: List<Chapter>,
    val limit: Int,
    val offset: Int,
    val total: Int
)

@Serializable
data class CoverResponse(
    val result: String,
    val data: CoverArt
)

@Serializable
data class AtHomeResponse(
    val result: String,
    val baseUrl: String,
    val chapter: ChapterHash
)

@Serializable
data class AuthorResponse(
    val result: String,
    val data: Author
)

// ===== DATA MODELS =====

@Serializable
data class Manga(
    val id: String,
    val type: String = "manga",
    val attributes: MangaAttributes,
    val relationships: List<Relationship> = emptyList()
) {
    val title: String
        get() = attributes.title.values.firstOrNull() ?: attributes.altTitles.firstOrNull()?.values?.firstOrNull() ?: "Sans titre"

    val description: String
        get() = attributes.description.values.firstOrNull() ?: ""

    val coverUrl: String?
        get() = relationships.firstOrNull { it.type == "cover_art" }?.attributes?.let { cover ->
            "https://uploads.mangadex.org/covers/${id}/${cover.fileName}.256.jpg"
        }

    val authorName: String
        get() = relationships.firstOrNull { it.type == "author" }?.attributes?.name
            ?: relationships.firstOrNull { it.type == "artist" }?.attributes?.name
            ?: "Inconnu"

    val status: String = attributes.status
    val year: Int? = attributes.year
    val tags: List<String> = attributes.tags.map { it.attributes.name.values.firstOrNull() ?: "" }
    val lastChapter: String? = attributes.lastChapter
}

@Serializable
data class MangaAttributes(
    val title: LocalizedString,
    val altTitles: List<LocalizedString> = emptyList(),
    val description: LocalizedString,
    val status: String,
    val year: Int?,
    val tags: List<Tag> = emptyList(),
    val lastChapter: String?,
    val lastVolume: String?,
    val contentRating: String,
    val createdAt: String,
    val updatedAt: String,
    val version: Int
)

@Serializable
data class LocalizedString(
    val en: String? = null,
    val fr: String? = null,
    val ja: String? = null,
    val ja_ro: String? = null,
    // Add more languages as needed
) {
    val values: List<String>
        get() = listOf(en, fr, ja, ja_ro).filterNotNull()
}

@Serializable
data class Tag(
    val id: String,
    val type: String = "tag",
    val attributes: TagAttributes,
    val relationships: List<Any> = emptyList()
)

@Serializable
data class TagAttributes(
    val name: LocalizedString,
    val description: LocalizedString,
    val group: String,
    val version: Int
)

@Serializable
data class Chapter(
    val id: String,
    val type: String = "chapter",
    val attributes: ChapterAttributes,
    val relationships: List<Relationship> = emptyList()
) {
    val chapterNumber: String = attributes.chapter ?: "0"
    val volumeNumber: String? = attributes.volume
    val title: String = attributes.title ?: ""
    val pageCount: Int = attributes.pages ?: 0
    val translatedLanguage: String = attributes.translatedLanguage
    val externalUrl: String? = attributes.externalUrl
    val publishAt: String = attributes.publishAt
    val scanlationGroup: String?
        get() = relationships.firstOrNull { it.type == "scanlation_group" }?.attributes?.name
}

@Serializable
data class ChapterAttributes(
    val title: String?,
    val volume: String?,
    val chapter: String?,
    val pages: Int?,
    val translatedLanguage: String,
    val externalUrl: String?,
    val publishAt: String,
    val readableAt: String,
    val createdAt: String,
    val updatedAt: String,
    val version: Int
)

@Serializable
data class ChapterHash(
    val hash: String,
    val data: List<String>,
    val dataSaver: List<String>
)

@Serializable
data class CoverArt(
    val id: String,
    val type: String = "cover_art",
    val attributes: CoverAttributes,
    val relationships: List<Any> = emptyList()
)

@Serializable
data class CoverAttributes(
    val description: String,
    val volume: String?,
    val fileName: String,
    val locale: String,
    val createdAt: String,
    val updatedAt: String,
    val version: Int
)

@Serializable
data class Relationship(
    val id: String,
    val type: String,
    val attributes: RelationshipAttributes? = null
)

@Serializable
data class RelationshipAttributes(
    val name: String? = null,
    val fileName: String? = null
)

@Serializable
data class Author(
    val id: String,
    val type: String = "author",
    val attributes: AuthorAttributes,
    val relationships: List<Any> = emptyList()
)

@Serializable
data class AuthorAttributes(
    val name: String,
    val imageUrl: String?,
    val version: Int
)

// ===== IMAGE URL BUILDER =====

object MangaDexImageUrl {
    private const val BASE_URL = "https://uploads.mangadex.org"
    private const val DATA_SAVER_QUALITY = 70

    fun coverUrl(mangaId: String, coverFileName: String, quality: CoverQuality = CoverQuality.MEDIUM): String {
        return "$BASE_URL/covers/$mangaId/$coverFileName.${quality.suffix}.jpg"
    }

    fun pageUrl(baseUrl: String, chapterHash: String, page: String, dataSaver: Boolean = false): String {
        val quality = if (dataSaver) "data-saver" else "data"
        return "$BASE_URL/$quality/$chapterHash/$page"
    }

    enum class CoverQuality {
        LOW("256"),
        MEDIUM("512"),
        HIGH("1024"),
        ORIGINAL("");

        val suffix: String
    }
}

// ===== JSON CONFIG =====

object MangaDexJson {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
        coerceInputValues = true
        useAlternativeNames = true
    }
}