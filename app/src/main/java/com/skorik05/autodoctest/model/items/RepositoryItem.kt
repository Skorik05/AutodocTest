package com.skorik05.autodoctest.model.items

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.skorik05.autodoctest.model.utils.parseDate


data class RepositoryItem (
    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("full_name")
    @Expose
    val fullName: String? = null,

    @SerializedName("owner")
    @Expose
    val owner: OwnerItem? = null,

    @SerializedName("description")
    @Expose
    val description: String? = null,

    @SerializedName("updated_at")
    @Expose
    val updatedAt: String? = null,

    @SerializedName("stargazers_count")
    @Expose
    val stargazersCount: Int? = null,

    @SerializedName("language")
    @Expose
    val languages: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(fullName)
        parcel.writeString(description)
        parcel.writeString(updatedAt)
        parcel.writeValue(stargazersCount)
        parcel.writeString(languages)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RepositoryItem> {
        override fun createFromParcel(parcel: Parcel): RepositoryItem {
            return RepositoryItem(parcel)
        }

        override fun newArray(size: Int): Array<RepositoryItem?> {
            return arrayOfNulls(size)
        }
    }

    fun getFormattedDate() : String {
        return parseDate(updatedAt)
    }
}