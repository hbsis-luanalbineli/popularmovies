package com.themovielist.model

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcel
import android.os.Parcelable

import com.themovielist.repository.data.MovieContract
import com.google.gson.annotations.SerializedName

import java.util.Date

data class MovieModel constructor(@SerializedName("id")
                                  var id: Int = 0,

                                  @SerializedName("poster_path")
                                  var posterPath: String = "",

                                  @SerializedName("overview")
                                  var overview: String = "",

                                  @SerializedName("title")
                                  var title: String = "",

                                  @SerializedName("vote_average")
                                  var voteAverage: Double = 0.0,

                                  @SerializedName("release_date")
                                  var releaseDate: Date? = null,

                                  @SerializedName("backdrop_path")
                                  var backdropPath: String = "",

                                  @SerializedName("vote_count")
                                  var voteCount: Int = 0) : Parcelable {

    private constructor(contentValues: ContentValues) : this(
            contentValues.getAsInteger(MovieContract.MovieEntry._ID),
            contentValues.getAsString(MovieContract.MovieEntry.COLUMN_POSTER_PATH),
            contentValues.getAsString(MovieContract.MovieEntry.COLUMN_OVERVIEW),
            contentValues.getAsString(MovieContract.MovieEntry.COLUMN_TITLE),
            contentValues.getAsDouble(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE),
            Date(contentValues.getAsLong(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)),
            contentValues.getAsString(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH),
            contentValues.getAsInteger(MovieContract.MovieEntry.COLUMN_VOTE_COUNT))

    constructor(cursor: Cursor) : this(
            cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID)),
            cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)),
            cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW)),
            cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)),
            cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE)),
            Date(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE))),
            cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH)),
            cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT)))

    private constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readDouble(),
            Date(parcel.readLong()),
            parcel.readString(),
            parcel.readInt())

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeInt(id)
        parcel.writeString(posterPath)
        parcel.writeString(overview)
        parcel.writeString(title)

        parcel.writeDouble(voteAverage)
        parcel.writeLong(releaseDate!!.time)

        parcel.writeString(backdropPath)
        parcel.writeInt(voteCount)
    }

    fun toContentValues(): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(MovieContract.MovieEntry._ID, id)

        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, posterPath)

        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview)

        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title)

        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage)

        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, releaseDate!!.time)

        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, backdropPath)

        contentValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, voteCount)

        return contentValues
    }

    companion object {
        val CREATOR: Parcelable.Creator<MovieModel> = object : Parcelable.Creator<MovieModel> {
            override fun createFromParcel(parcel: Parcel): MovieModel {
                return MovieModel(parcel)
            }

            override fun newArray(size: Int): Array<MovieModel> {
                return Array(size) { MovieModel() }
            }
        }

        fun fromContentValues(contentValues: ContentValues): MovieModel {
            return MovieModel(contentValues)
        }

        fun fromCursor(cursor: Cursor): MovieModel {
            return MovieModel(cursor)
        }
    }
}