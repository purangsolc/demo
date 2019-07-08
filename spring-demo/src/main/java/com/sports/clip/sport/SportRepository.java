package com.sports.clip.sport;

import org.springframework.data.mongodb.repository.MongoRepository;

interface SportRepository extends MongoRepository<Sport, Long> {
}
