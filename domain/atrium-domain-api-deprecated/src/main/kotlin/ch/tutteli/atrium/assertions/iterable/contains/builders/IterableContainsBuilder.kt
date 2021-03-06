@file:Suppress("DEPRECATION" /* TODO remove with 1.0.0*/)
package ch.tutteli.atrium.assertions.iterable.contains.builders

import ch.tutteli.atrium.assertions.Assertion
import ch.tutteli.atrium.assertions.basic.contains.builders.ContainsBuilder
import ch.tutteli.atrium.creating.AssertionPlant
import ch.tutteli.atrium.creating.SubjectProvider
import ch.tutteli.atrium.domain.creating.iterable.contains.IterableContains

/**
 * Represents the *deprecated* entry point of the fluent API of sophisticated `contains` assertions.
 * It contains the [subjectProvider] for which the [Assertion] shall be build as well as the decoration behaviour which shall be
 * applied to the [subjectProvider]'s [subject][SubjectProvider.subject].
 *
 * @param T The input type of the search which is the same as the type of the [subject][SubjectProvider.subject] of the
 *   [subjectProvider].
 * @param S The search behaviour which should be applied for the input of the search.
 *
 * @constructor Represents the entry point of the fluent API of sophisticated `contains` assertions.
 * @param plant The [AssertionPlant] for which the sophisticated `contains` assertions shall be built.
 * @param searchBehaviour The search behaviour which shall be applied to the input of the search.
 */
@Deprecated(
    "Use the interface IterableContains.Builder instead; will be removed with 1.0.0",
    ReplaceWith(
        "IterableContains.Builder",
        "ch.tutteli.atrium.domain.creating.iterable.contains.IterableContains"
    )
)
open class IterableContainsBuilder<out E, out T : Iterable<E>, out S : IterableContains.SearchBehaviour>(
    plant: AssertionPlant<T>, searchBehaviour: S
) : ContainsBuilder<T, S>(plant, searchBehaviour),
    IterableContains.Builder<E, T, S>
