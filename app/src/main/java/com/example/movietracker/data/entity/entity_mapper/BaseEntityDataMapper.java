package com.example.movietracker.data.entity.entity_mapper;

public abstract class BaseEntityDataMapper<ListEntity, Entity> {
    public abstract ListEntity transformToList(Entity entity);
    public abstract Entity transformFromList(ListEntity entity);
}
