package com.pointlogic.weaver.service

import java.time.LocalDateTime

import com.pointlogic.weaver.domain.model.Binary

class BinaryService {
  def upload(name: String, payload: Array[Byte]): Unit = {
    //new Binary(name, payload, LocalDateTime.now())
  }

  def fetch(name: String): Binary = {
    null
  }
}
