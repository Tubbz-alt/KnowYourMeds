package com.tompee.utilities.knowyourmeds.core.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


//region getRxNormId
@JsonIgnoreProperties(ignoreUnknown = true)
data class RxNormModel(var idGroup: IdGroup? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class IdGroup(
        var name: String = "",
        var rxnormId: List<String> = listOf()
)

//endregion

//region getName
@JsonIgnoreProperties(ignoreUnknown = true)
data class PropertyGroup(
        var propConceptGroup: PropConceptGroup? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PropConceptGroup(
        var propConcept: List<PropConcept> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PropConcept(
        var propCategory: String = "",
        var propName: String = "",
        var propValue: String = ""
)
//endregion

//region getSuggestion

@JsonIgnoreProperties(ignoreUnknown = true)
data class SuggestionModel(
        var suggestionGroup: SuggestionGroup? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SuggestionGroup(
        var name: String = "",
        var suggestionList: Suggestion? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Suggestion(
        var suggestion: List<String> = listOf()
)
//endregion

//region getMedlineUrl
@JsonIgnoreProperties(ignoreUnknown = true)
data class MedlineUrlModel(
        var feed: Feed? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Feed(
        var entry: List<Entry> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Entry(
        var link: List<Link> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Link(
        var href: String = ""
)
//endregion

//region getSpl
@JsonIgnoreProperties(ignoreUnknown = true)
data class SplModel(
        var data: List<Data> = listOf(),
        var metadata: Metadata? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Data(
        var setid: String = "",
        var title: String = "",
        var published_date: String = "",
        var spl_version: Int = 0
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Metadata(
        var total_pages: Int = 0,
        var current_page: Int = 0
)
//endregion

//region getTtyValues
@JsonIgnoreProperties(ignoreUnknown = true)
data class TtyGroup(
        var relatedGroup: RelatedGroup? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class RelatedGroup(
        var conceptGroup: List<ConceptGroup> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConceptGroup(
        var tty: String = "",
        var conceptProperties: List<ConceptProperties> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConceptProperties(
        var name: String = ""
)

//endregion

//region getInteraction

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionModel(
        var interactionTypeGroup: List<InteractionTypeGroup> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionTypeGroup(
        var interactionType: List<InteractionType> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionType(
        var interactionPair: List<InteractionPair> = listOf()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionPair(
        var interactionConcept: List<InteractionConcept> = listOf(),
        var description: String = ""
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionConcept(
        var sourceConceptItem: SourceConceptItem? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SourceConceptItem(
        var name: String = ""
)

//endregion