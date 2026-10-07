package com.dut.campusconnect
data class User(val id: String = "", val email: String = "", val name: String = "", val role: String = "student")
data class PastPaper(val id: String = "", val moduleCode: String = "", val year: Int = 2024, val title: String = "", val verified: Boolean = false, val uploadedBy: String = "")
data class ModuleCircle(val id: String = "", val name: String = "", val moduleCode: String = "")
data class ChatMessage(val id: String = "", val circleId: String = "", val sender: String = "", val message: String = "", val time: Long = System.currentTimeMillis())
