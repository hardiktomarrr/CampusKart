package com.example.campuskart.data

object SampleData {
    fun getInitialListings(): List<ListingEntity> {
        return listOf(
            ListingEntity(
                id = 1L,
                title = "Introduction to Algorithms",
                description = "CLRS 3rd Edition, good condition.",
                price = 450.0,
                category = "Books",
                condition = "Used - Good",
                imageResName = "item_book",
                sellerName = "Parnika Joshi",
                sellerPhone = "8937954898",
                ownerId = "user_1"
            ),
            ListingEntity(
                id = 2L,
                title = "Scientific Calculator",
                description = "Casio fx-991EX. Barely used.",
                price = 750.0,
                category = "Electronics",
                condition = "Like New",
                imageResName = "item_calculator",
                sellerName = "Parnika Joshi",
                sellerPhone = "8937954898",
                ownerId = "user_2"
            ),
            ListingEntity(
                id = 3L,
                title = "Dorm Mini Fridge",
                description = "Works perfectly, moving out sale.",
                price = 3000.0,
                category = "Appliances",
                condition = "Used - Acceptable",
                imageResName = "item_fridge",
                sellerName = "Parnika Joshi",
                sellerPhone = "8937954898",
                ownerId = "user_3"
            ),
            ListingEntity(
                id = 4L,
                title = "Bicycle",
                description = "Mountain bike, needs new tires.",
                price = 4500.0,
                category = "Sports",
                condition = "Used - Fair",
                imageResName = "item_cycle",
                sellerName = "Parnika Joshi",
                sellerPhone = "8937954898",
                ownerId = "user_4"
            ),
            ListingEntity(
                id = 5L,
                title = "Study Desk",
                description = "IKEA desk, very sturdy.",
                price = 2400.0,
                category = "Furniture",
                condition = "Used - Good",
                imageResName = "item_desk",
                sellerName = "Parnika Joshi",
                sellerPhone = "8937954898",
                ownerId = "user_5"
            ),
            ListingEntity(
                id = 6L,
                title = "Noise Cancelling Headphones",
                description = "Sony headphones, great for studying.",
                price = 12000.0,
                category = "Electronics",
                condition = "Like New",
                imageResName = "shopping",
                sellerName = "Parnika Joshi",
                sellerPhone = "8937954898",
                ownerId = "user_6"
            )
        )
    }
}