package com.viewlift.models.billing.utils

import com.viewlift.views.fragments.BillingFragment

data class Payment(val name: BillingFragment.PaymentMethod,
                   val image: Int)