package com.kimmandoo.tv.part7to9

import com.kimmandoo.tv.Movie

val movieList = mutableListOf<Movie>().apply {
    repeat(10) { idx ->
        when {
            idx % 3 == 0 -> {
                add(
                    Movie(
                        id = idx.toLong(),
                        title = "Movie $idx",
                        studio = "Studio $idx",
                        imageUri = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQszAySd5NzSxVsfOR6ZllimOYiz0KrOEhgCw&s",
                        cardImageUrl = "https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg",
                        videoUrl = "https://media.githubusercontent.com/media/Fraunhofer-IIS/mpegh-test-content/main/TRI_Fileset_17_514H_D1_D2_D3_O1_24bit1080p50.mp4"
                    )
                )
            }
            idx % 3 == 1 -> {
                add(
                    Movie(
                        id = idx.toLong(),
                        title = "Movie $idx",
                        studio = "Studio $idx",
                        imageUri = "https://t4.ftcdn.net/jpg/05/78/60/99/360_F_578609973_kGyUqmJ2Gl9wGqag78ciyDbUFaB2hdU5.jpg",
                        cardImageUrl = "https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg",
                        videoUrl = "https://media.githubusercontent.com/media/Fraunhofer-IIS/mpegh-test-content/main/TRI_Fileset_17_514H_D1_D2_D3_O1_24bit1080p50.mp4"
                    )
                )
            }
            else -> {
                add(
                    Movie(
                        id = idx.toLong(),
                        title = "Title: $idx",
                        studio = "Studio $idx",
                        imageUri = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjStS_8E0EjFPkUmCuOHBnr8o2C2jiX-D8_Q&s",
                        cardImageUrl = "https://www.alleycat.org/wp-content/uploads/2019/03/FELV-cat.jpg",
                        videoUrl = "https://media.githubusercontent.com/media/Fraunhofer-IIS/mpegh-test-content/main/TRI_Fileset_17_514H_D1_D2_D3_O1_24bit1080p50.mp4"
                    )
                )
            }
        }
    }
}