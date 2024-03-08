package com.isaev.dummyjson

import coil.request.ImageResult
import coil.request.SuccessResult
import coil.transition.CrossfadeTransition
import coil.transition.Transition
import coil.transition.TransitionTarget

// Crossfade even if from memory
class CrossfadeTransitionFactoryFixed : Transition.Factory {
    override fun create(target: TransitionTarget, result: ImageResult): Transition {
        if (result !is SuccessResult) {
            return Transition.Factory.NONE.create(target, result)
        }

        return CrossfadeTransition(target, result)
    }
}