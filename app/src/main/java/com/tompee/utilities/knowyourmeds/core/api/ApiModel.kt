package com.tompee.utilities.knowyourmeds.core.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


//region getRxNormId
@JsonIgnoreProperties(ignoreUnknown = true)
data class RxNormModel(
        @JsonProperty("idGroup")
        var group: IdGroup? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class IdGroup(
        @JsonProperty("name")
        var name: String = "",

        @JsonProperty("rxnormId")
        var idList: List<String> = listOf()
)

//endregion

//region getName
@JsonIgnoreProperties(ignoreUnknown = true)
data class PropertyGroup(
        @JsonProperty("propConceptGroup")
        var group: PropConceptGroup? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PropConceptGroup(
        @JsonProperty("propConcept")
        var list: List<PropConcept> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PropConcept(
        @JsonProperty("propCategory")
        var category: String = "",

        @JsonProperty("propName")
        var name: String = "",

        @JsonProperty("propValue")
        var value: String = ""
)
//endregion

//region getSuggestion

@JsonIgnoreProperties(ignoreUnknown = true)
data class SuggestionModel(
        @JsonProperty("suggestionGroup")
        var group: SuggestionGroup? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SuggestionGroup(
        @JsonProperty("name")
        var name: String = "",

        @JsonProperty("suggestionList")
        var suggestion: Suggestion? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Suggestion(
        @JsonProperty("suggestion")
        var list: List<String> = listOf()
)
//endregion

//region getMedlineUrl
@JsonIgnoreProperties(ignoreUnknown = true)
data class MedlineUrlModel(
        @JsonProperty("feed")
        var feed: Feed? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Feed(
        @JsonProperty("entry")
        var entryList: List<Entry> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Entry(
        @JsonProperty("link")
        var linkList: List<Link> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Link(
        @JsonProperty("href")
        var url: String = ""
)
//endregion

//region getSpl
@JsonIgnoreProperties(ignoreUnknown = true)
data class SplModel(
        @JsonProperty("data")
        var dataList: List<Data> = listOf(),

        @JsonProperty("metadata")
        var metadata: Metadata? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Data(
        @JsonProperty("setid")
        var setId: String = "",

        @JsonProperty("title")
        var title: String = "",

        @JsonProperty("published_date")
        var date: String = "",

        @JsonProperty("spl_version")
        var version: Int = 0
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Metadata(
        @JsonProperty("total_pages")
        var total: Int = 0,

        @JsonProperty("current_page")
        var current: Int = 0
)
//endregion

//region getTtyValues
@JsonIgnoreProperties(ignoreUnknown = true)
data class TtyGroup(
        @JsonProperty("relatedGroup")
        var relatedGroup: RelatedGroup? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RelatedGroup(
        @JsonProperty("conceptGroup")
        var conceptGroupList: List<ConceptGroup> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConceptGroup(
        @JsonProperty("tty")
        var type: String = "",

        @JsonProperty("conceptProperties")
        var propertyList: List<ConceptProperties> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConceptProperties(
        @JsonProperty("name")
        var name: String = "",

        @JsonProperty("rxcui")
        var id: String = ""
)

//endregion

//region getInteraction

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionModel(
        @JsonProperty("interactionTypeGroup")
        var typeGroupList: List<InteractionTypeGroup> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionTypeGroup(
        @JsonProperty("interactionType")
        var typeList: List<InteractionType> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionType(
        @JsonProperty("interactionPair")
        var pairList: List<InteractionPair> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionPair(
        @JsonProperty("interactionConcept")
        var conceptList: List<InteractionConcept> = listOf(),

        @JsonProperty("description")
        var description: String = ""
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionConcept(
        @JsonProperty("sourceConceptItem")
        var source: SourceConceptItem? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SourceConceptItem(
        @JsonProperty("name")
        var name: String = "",

        @JsonProperty("url")
        var url: String = ""
)

//endregion