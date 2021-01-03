package nolambda.github.usersearch

import android.content.Context
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*
import nolambda.github.usersearch.data.User
import nolambda.kommonadapter.map
import nolambda.kommonadapter.simple.SimpleAdapter

class UserAdapter(context: Context) : SimpleAdapter(context) {
    init {
        create {
            map<User>(R.layout.item_user) { vh, item ->
                Glide.with(vh.itemView.context)
                    .load(item.avatarUrl)
                    .circleCrop()
                    .into(vh.itemView.imgView)

                vh.itemView.txtName.text = item.login
            }
        }
    }
}