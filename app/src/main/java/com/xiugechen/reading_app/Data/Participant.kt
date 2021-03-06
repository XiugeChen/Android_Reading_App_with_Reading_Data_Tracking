package com.xiugechen.reading_app.Data

class Participant {
    var mFullname: String = ""

    private var mGender: String = ""

    constructor (fullname: String, gender: String) {
        this.mFullname = fullname
        this.mGender = gender
    }

    override fun toString(): String {
        return "%s,%s".format(mFullname, mGender)
    }

    fun isSet(): Boolean {
        return !mFullname.isEmpty() && !mGender.isEmpty()
    }
}