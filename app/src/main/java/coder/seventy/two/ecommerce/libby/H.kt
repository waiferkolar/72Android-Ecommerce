package coder.seventy.two.ecommerce.libby

import android.util.Log

class H {
    companion object {
        var cartMap: HashMap<Int, Int> = hashMapOf()

        var USER_TOKEN: String? = null

        fun l(msg: String) {
            Log.d("my_message", msg)
        }

        fun checkUserAuth(): Boolean {
            return USER_TOKEN == ""
        }

        fun addToCart(key: Int) {
            if (cartMap.containsKey(key)) {
                addCountCart(key)
            } else {
                cartMap[key] = 1
            }
        }

        fun removeFromCart(key: Int) {
            if (cartMap.containsKey(key)) {
                cartMap.remove(key)
            }
        }

        fun clearCart() {
            cartMap.clear()
        }

        fun getCartCount(): Int {
            return cartMap.size
        }

        fun getSingItemCout(key: Int): Int {
            if (cartMap.containsKey(key)) {
                return cartMap[key]!!
            }
            return 0
        }

        fun addCountCart(key: Int) {
            val oldCount = cartMap[key]!!
            val newCount = oldCount + 1
            cartMap[key] = newCount
        }

        fun getAllKeys(): String {
            var keys = ""
            for ((k, v) in cartMap) {
                keys += "$k#$v,"
            }
            return keys
        }


    }
}