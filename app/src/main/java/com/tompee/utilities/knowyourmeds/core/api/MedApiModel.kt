package com.tompee.utilities.knowyourmeds.core.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


//region getRxNormId
@JsonIgnoreProperties(ignoreUnknown = true)
data class RxNormModel(var idGroup: IdGroup? = null)

@JsonIgnoreProperties(ignoreUnknown = true)
data class IdGroup(
        var name: String? = null,
        var rxnormId: List<String>? = null
)

//endregion

//region getPropertyName
@JsonIgnoreProperties(ignoreUnknown = true)
data class PropertyGroup(
        var propConceptGroup: PropConceptGroup? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PropConceptGroup(
        var propConcept: List<PropConcept>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class PropConcept(
        var propCategory: String? = null,
        var propName: String? = null,
        var propValue: String? = null
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
        var suggestion: List<String>? = null
)
//endregion

//region getMedlineUrl
@JsonIgnoreProperties(ignoreUnknown = true)
data class MedlineUrlModel(
        var feed: Feed? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Feed(
        var entry: List<Entry>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Entry(
        var link: List<Link>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Link(
        var href: String? = null
)
//endregion

//region getSpl
@JsonIgnoreProperties(ignoreUnknown = true)
data class SplModel(
        var data: List<Data>? = null,
        var metadata: Metadata? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Data(
        var setid: String? = null,
        var title: String? = null
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
        var conceptGroup: List<ConceptGroup>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConceptGroup(
        var tty: String = "",
        var conceptProperties: List<ConceptProperties>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ConceptProperties(
        var name: String = ""
)

//endregion

//region getInteraction

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionModel(
        var interactionTypeGroup: List<InteractionTypeGroup>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionTypeGroup(
        var interactionType: List<InteractionType>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionType(
        var interactionPair: List<InteractionPair>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class InteractionPair(
        var interactionConcept: List<InteractionConcept>? = null,
        var description: String? = null
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