package com.tompee.utilities.knowyourmeds.model

import io.reactivex.Scheduler

data class SchedulerPool(val io: Scheduler,
                         val main: Scheduler,
                         val computation: Scheduler,
                         val trampoline: Scheduler)