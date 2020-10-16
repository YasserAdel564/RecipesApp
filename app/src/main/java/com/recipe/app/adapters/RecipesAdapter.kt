package com.recipe.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.recipe.app.R
import com.recipe.app.data.models.RecipeModel
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.recipe_item.view.*
import java.util.*

class RecipesAdapter internal constructor(
    apps: List<RecipeModel>,
    mContext: Context,
    mCallback: AdapterCallback
) : RecyclerView.Adapter<RecipesAdapter.AppViewHolder>(), Filterable {


    private val mContext: Context = mContext
    private val mCallback: AdapterCallback = mCallback
    private val mData: List<RecipeModel> = apps
    var mDataFiltered: List<RecipeModel> = apps

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.recipe_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {

        val model: RecipeModel = mDataFiltered[position]
        Glide.with(mContext).load(model.image).into(holder.recipeImage)
        holder.recipeName.text = model.name
        holder.recipeDesc.text = model.description
        holder.recipeCalories.text = model.calories
        holder.recipeItem.setOnClickListener { mCallback.itemClicked(model) }

    }

    override fun getItemCount(): Int {
        return mDataFiltered.size
    }

    class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeItem: CardView = itemView.recipe_item
        val recipeImage: CircleImageView = itemView.recipe_image
        val recipeName: TextView = itemView.recipe_name
        val recipeDesc: TextView = itemView.recipe_desc
        val recipeCalories: TextView = itemView.recipe_calories


    }

    interface AdapterCallback {
        fun itemClicked(model: RecipeModel)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val Key = constraint.toString()
                if (Key.isEmpty()) {
                    mDataFiltered = mData

                } else {
                    val lstFiltered: MutableList<RecipeModel> = ArrayList<RecipeModel>()
                    for (row in mData) {
                        if (row.name!!.toLowerCase().contains(Key.toLowerCase())) {
                            lstFiltered.add(row)
                        }
                    }
                    mDataFiltered = lstFiltered
                }
                val filterResults = FilterResults()
                filterResults.values = mDataFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                mDataFiltered = results.values as ArrayList<RecipeModel>
                notifyDataSetChanged()
            }
        }
    }

}