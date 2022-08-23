package com.example.newsapp.data.models

import com.google.gson.annotations.SerializedName

data class ArticleModel(
    @SerializedName("uri")
    var uri: String? = null,
    @SerializedName("url")
    var url: String? = null,
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("asset_id")
    var assetId: Long? = null,
    @SerializedName("source")
    var source: String? = null,
    @SerializedName("published_date")
    var publishedDate: String? = null,
    @SerializedName("updated")
    var updated: String? = null,
    @SerializedName("section")
    var section: String? = null,
    @SerializedName("subsection")
    var subsection: String? = null,
    @SerializedName("nytdsection")
    var nytdsection: String? = null,
    @SerializedName("adx_keywords")
    var adxKeywords: String? = null,
    @SerializedName("column")
    var column: String? = null,
    @SerializedName("byline")
    var byline: String? = null,
    @SerializedName("type")
    var type: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("abstract")
    var abstract: String? = null,
    @SerializedName("des_facet")
    var desFacet: ArrayList<String>,
    @SerializedName("org_facet")
    var orgFacet: ArrayList<String>,
    @SerializedName("per_facet")
    var perFacet: ArrayList<String>,
    @SerializedName("geo_facet")
    var geoFacet: ArrayList<String>,
    @SerializedName("media")
    var media: ArrayList<MediaModel>,
    @SerializedName("eta_id")
    var etaId: Int? = null,
    )