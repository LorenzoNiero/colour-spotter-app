package com.challenge.colour_spotter.database.model

interface IEntity {

}

abstract class BaseEntity : IEntity {
    abstract val id: String
}