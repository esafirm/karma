package nolambda.github.usersearch

import android.content.Context
import com.bumptech.glide.Glide
import nolambda.github.usersearch.data.User
import nolambda.github.usersearch.databinding.ItemUserBinding
import nolambda.kommonadapter.map
import nolambda.kommonadapter.simple.SimpleAdapter

class UserAdapter(context: Context) : SimpleAdapter(context) {
    init {
        create {
            map<User>(R.layout.item_user) { vh, item ->
                val binding = ItemUserBinding.bind(vh.itemView)

                Glide.with(vh.itemView.context)
                    .load(item.avatarUrl)
                    .circleCrop()
                    .into(binding.imgView)

                binding.txtName.text = item.login
            }
        }
    }
}
