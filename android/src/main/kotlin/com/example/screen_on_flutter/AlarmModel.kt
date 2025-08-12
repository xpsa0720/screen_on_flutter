package com.example.screen_on_flutter
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class AlarmModel(
    val title:String,
    val content:String,
) : Parcelable {
    companion object{
        fun fromMap(map:Map<*,*>?): AlarmModel{
            val m = map as? Map<String,Any?>?: emptyMap()
            return AlarmModel(
                title = (m["title"] as? String)?:"screen_on_flutter",
                content = (m["content"] as? String)?:"Start foreground",
        }
    }
}