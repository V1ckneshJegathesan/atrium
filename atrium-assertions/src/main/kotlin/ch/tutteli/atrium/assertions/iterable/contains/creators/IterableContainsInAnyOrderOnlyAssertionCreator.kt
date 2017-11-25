package ch.tutteli.atrium.assertions.iterable.contains.creators

import ch.tutteli.atrium.assertions.*
import ch.tutteli.atrium.assertions.DescriptionIterableAssertion.*
import ch.tutteli.atrium.assertions.iterable.contains.IIterableContains
import ch.tutteli.atrium.assertions.iterable.contains.decorators.IterableContainsInAnyOrderOnlyDecorator
import ch.tutteli.atrium.creating.IAssertionPlant
import ch.tutteli.atrium.reporting.RawString
import ch.tutteli.atrium.reporting.translating.Untranslatable

abstract class IterableContainsInAnyOrderOnlyAssertionCreator<E, T : Iterable<E>, S>(
    private val decorator: IterableContainsInAnyOrderOnlyDecorator
) : IIterableContains.ICreator<T, S> {

    override final fun createAssertionGroup(plant: IAssertionPlant<T>, searchCriterion: S, otherSearchCriteria: Array<out S>): IAssertionGroup {
        return LazyThreadUnsafeAssertionGroup {
            val list = plant.subject.toMutableList()
            val actualSize = list.size
            val assertions = mutableListOf<IAssertion>()
            val allSearchCriteria = listOf(searchCriterion, *otherSearchCriteria)

            val mismatches = createAssertionsForAllSearchCriteria(allSearchCriteria, list, assertions)
            val featureAssertions = createSizeFeatureAssertion(allSearchCriteria, actualSize)
            if (mismatches == 0 && list.isNotEmpty()) {
                featureAssertions.add(LazyThreadUnsafeAssertionGroup {
                    createExplanatoryGroupForMismatchesEtc(list, WARNING_ADDITIONAL_ENTRIES)
                })
            }
            assertions.add(AssertionGroup(FeatureAssertionGroupType, Untranslatable(list::size.name), RawString(actualSize.toString()), featureAssertions))

            val description = decorator.decorateDescription(CONTAINS)
            val summary = AssertionGroup(SummaryAssertionGroupType, description, RawString(""), assertions.toList())
            if (mismatches != 0 && list.isNotEmpty()) {
                val warningDescription = when (list.size) {
                    mismatches -> WARNING_MISMATCHES
                    else -> WARNING_MISMATCHES_ADDITIONAL_ENTRIES
                }
                InvisibleAssertionGroup(listOf(
                    summary,
                    createExplanatoryGroupForMismatchesEtc(list, warningDescription)
                ))
            } else {
                summary
            }
        }
    }

    private fun createAssertionsForAllSearchCriteria(allSearchCriteria: List<S>, list: MutableList<E>, assertions: MutableList<IAssertion>): Int {
        var mismatches = 0
        allSearchCriteria.forEach {
            val (found, assertion) = createAssertionForSearchCriterionAndRemoveMatchFromList(it, list)
            if (!found) ++mismatches
            assertions.add(assertion)
        }
        return mismatches
    }

    protected abstract fun createAssertionForSearchCriterionAndRemoveMatchFromList(searchCriterion: S, list: MutableList<E>): Pair<Boolean, IAssertion>

    private fun createSizeFeatureAssertion(allSearchCriteria: List<S>, actualSize: Int): MutableList<IAssertion>
        = mutableListOf(BasicAssertion(DescriptionAnyAssertion.TO_BE, RawString(allSearchCriteria.size.toString()), { actualSize == allSearchCriteria.size }))

    private fun createExplanatoryGroupForMismatchesEtc(list: MutableList<E>, warning: DescriptionIterableAssertion): ExplanatoryAssertionGroup {
        val assertions = list.map { ExplanatoryAssertion(it) }
        val additionalEntries = AssertionGroup(ListAssertionGroupType, warning, RawString(""), assertions)
        return ExplanatoryAssertionGroup(WarningAssertionGroupType, listOf(additionalEntries))
    }
}
