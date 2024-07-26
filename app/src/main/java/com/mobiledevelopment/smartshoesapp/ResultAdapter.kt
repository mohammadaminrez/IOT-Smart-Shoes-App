package com.mobiledevelopment.smartshoesapp


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultAdapter(private val context: Context) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {
    private var data = listOf<Result>()


    fun updateData(newData: List<Result>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_result, parent, false)
        return ResultViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = data[position]
        holder.bind(result)
    }

    override fun getItemCount() = data.size

    inner class ResultViewHolder(itemView: View, private val context: Context) :
        RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.variableName)
        private val valueTextView: TextView = itemView.findViewById(R.id.variableValue)
        private val contextTextView: TextView = itemView.findViewById(R.id.contextValue)
        private val locationIconView: ImageView = itemView.findViewById(R.id.locationIcon)

        private val customNames = mapOf(
            "lat" to "Latitude",
            "lng" to "Longitude",
            "status" to "Status",
            "heart_rate" to "Heart Rate",
            "distance" to "Distance",
            "battery" to "Battery",
            "pressure" to "Pressure",
            "temperature" to "Temperature",
            "humidity" to "Humidity",
            "location" to "Location",
            "speed" to "Speed",
            "time" to "Time",
            "date" to "Date",
            "altitude" to "Altitude",

            )

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val result = data[position]
                    if (result.name == "location") {
                        val intent = Intent(context, LocationActivity::class.java)
                        context.startActivity(intent)
                    }
                }
            }
        }

        fun bind(result: Result) {
            nameTextView.text = customNames[result.name] ?: result.name

            // Reset visibility states for consistent display
            locationIconView.visibility = View.GONE
            contextTextView.visibility = View.GONE
            valueTextView.visibility = View.VISIBLE

            when (result.name) {
                "date" -> {
                    val day = result.lastValue.context["day"]?.let { it as? Double }?.toInt()
                    val month = result.lastValue.context["month"]?.let { it as? Double }?.toInt()
                    val year = result.lastValue.context["year"]?.let { it as? Double }?.toInt()

                    if (day != null && month != null && year != null) {
                        val formattedDate = "$day/$month/$year"
                        valueTextView.text = formattedDate
                    } else {
                        valueTextView.text = "Date information incomplete"
                    }
                }

                "time" -> {
                    val hour = result.lastValue.context["hour"]?.let { it as? Double }?.toInt()
                    val minute = result.lastValue.context["minute"]?.let { it as? Double }?.toInt()
                    val second = result.lastValue.context["second"]?.let { it as? Double }?.toInt()

                    if (hour != null && minute != null && second != null) {
                        val formattedTime = String.format("%02d:%02d:%02d", hour, minute, second)
                        valueTextView.text = formattedTime
                    } else {
                        valueTextView.text = "Time information incomplete"
                    }
                }

                else -> {
                    val valueText = result.lastValue.value.toString() + (result.unit ?: "")
                    valueTextView.text = valueText

                    if (result.name == "location") {
                        locationIconView.visibility = View.VISIBLE
                    }

                    if (result.lastValue.context.isNotEmpty()) {
                        val contextValues = result.lastValue.context.map { entry ->
                            val customName = customNames[entry.key] ?: entry.key
                            "$customName: ${entry.value}"
                        }.joinToString(", ")
                        contextTextView.text = contextValues
                        contextTextView.visibility = View.VISIBLE
                        valueTextView.visibility = View.GONE // Hide valueTextView if context is displayed
                    }
                }
            }
        }
    }
}
