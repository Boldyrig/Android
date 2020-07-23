package com.gmail.fuskerr63.android.library.unitTest

import com.gmail.fuskerr63.java.entity.BirthdayCalendar
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.entity.ContactInfo
import com.gmail.fuskerr63.java.interactor.NotificationInteractorImpl
import com.gmail.fuskerr63.java.interactor.NotificationRepository
import com.gmail.fuskerr63.java.interactor.NotificationTime
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.URI
import java.util.*

class BirthdayUnitTest: Spek({
    @MockK
    val notificationTime = mockk<NotificationTime>()
    @MockK
    val notificationRepository = mockk<NotificationRepository>()

    val textNotification = "Today is the birthday of"
    val contactId = 1
    val contactName = "Иван"
    val flagUpdateCurrent = 134217728
    val flagNoCreate = 536870912

    val year1988 = 1988
    val year1990 = 1990
    val year1999 = 1999
    val year2000 = 2000

    val day7 = 7
    val day8 = 8
    val day29 = 29

    val birthday: Calendar = GregorianCalendar()
    val currentCalendar: Calendar = GregorianCalendar()
    val nextBirthday: Calendar = GregorianCalendar()

    Feature("#Тестируется NotificationInteractor") {
        val notificationInteractor = NotificationInteractorImpl(
                notificationTime,
                notificationRepository,
                textNotification,
                flagNoCreate,
                flagUpdateCurrent
        )

        every { notificationTime.currentTimeCalendar } returns currentCalendar

        lateinit var contact: Contact

        Scenario("Успешное добавление напоминания, День Рождения в текущем году был") {
            Given("Текущий год - 1999(не високосный) 9 сентября") {
                val day9 = 9
                currentCalendar.set(year1999, Calendar.SEPTEMBER, day9)
            }

            And("Есть контакт Иван с датой рождения 8 сентября") {
                birthday.set(year1990, Calendar.SEPTEMBER, day8, 0, 0, 0)
                contact = Contact(
                        contactId,
                        URI.create(""),
                        ContactInfo(
                                contactName,
                                "",
                                "",
                                "",
                                ""
                        ),
                        birthday,
                        ""
                )
            }

            And("И напоминание для этого контакта отсутствует") {
                every { notificationRepository.alarmIsUp(
                        contactId,
                        textNotification + contactName,
                        flagNoCreate
                ) } returns false
            }

            nextBirthday.set(year2000, Calendar.SEPTEMBER, day8, 0, 0, 0)

            val birthdayCalendar = BirthdayCalendar(
                    nextBirthday[Calendar.YEAR],
                    nextBirthday[Calendar.MONTH],
                    nextBirthday[Calendar.DATE],
                    nextBirthday[Calendar.HOUR],
                    nextBirthday[Calendar.MINUTE],
                    nextBirthday[Calendar.SECOND]
            )

            When("Когда переключается напоминание для контатка Иван") {
                every { notificationRepository.setAlarm(
                        birthdayCalendar,
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                ) } just Runs

                notificationInteractor.toggleNotificationForContact(contact)
            }

            Then("Тогда  происходит успешное добавление напоминания на 2000 год 8 сентября") {
                verify { notificationRepository.setAlarm(
                        birthdayCalendar,
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                ) }
            }
        }

        Scenario("Успешное добавление напоминания, Дня Рождения еще в текущем году не было") {
            Given("Текущий год - 1999(не високосный) 7 сентября") {
                currentCalendar.set(year1999, Calendar.SEPTEMBER, day7)
            }

            And("Есть контакт Иван с датой рождения 8 сентября") {
                birthday.set(year1990, Calendar.SEPTEMBER, day8, 0, 0, 0)
                contact = Contact(
                        contactId,
                        URI.create(""),
                        ContactInfo(
                                contactName,
                                "",
                                "",
                                "",
                                ""),
                        birthday,
                        "")
            }

            And("И напоминание для этого контакта отсутствует") {
                every { notificationRepository.alarmIsUp(
                        contactId,
                        textNotification + contactName,
                        flagNoCreate
                ) } returns false
            }

            nextBirthday.set(year1999, Calendar.SEPTEMBER, day8, 0, 0, 0)

            val birthdayCalendar = BirthdayCalendar(
                    nextBirthday[Calendar.YEAR],
                    nextBirthday[Calendar.MONTH],
                    nextBirthday[Calendar.DATE],
                    nextBirthday[Calendar.HOUR],
                    nextBirthday[Calendar.MINUTE],
                    nextBirthday[Calendar.SECOND]
            )

            When("Когда переключается напоминание для контатка Иван") {
                every { notificationRepository.setAlarm(
                        birthdayCalendar,
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                ) } just Runs

                notificationInteractor.toggleNotificationForContact(contact)
            }

            Then("Тогда  происходит успешное добавление напоминания на 1999 год 8 сентября") {
                verify { notificationRepository.setAlarm(
                        birthdayCalendar,
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                ) }
            }
        }

        Scenario("Успешное удаление напоминания") {
            Given("Текущий год - 1999(не високосный)") {
                currentCalendar.set(year1999, Calendar.SEPTEMBER, day7)
            }

            And("Есть контакт Иван с датой рождения 8 сентября") {
                birthday.set(year1990, Calendar.SEPTEMBER, day8, 0, 0, 0)

                contact = Contact(
                        contactId,
                        URI.create(""),
                        ContactInfo(
                                contactName,
                                "",
                                "",
                                "",
                                ""),
                        birthday,
                        "")
            }

            And("И для него включено напоминание на 2000 год 8 сентября") {
                every { notificationRepository.alarmIsUp(
                        contactId,
                        textNotification + contactName,
                        flagNoCreate
                ) } returns true
            }

            When("Когда переключается напоминание для контатка Иван") {
                every { notificationRepository.cancelAlarm(
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                ) } just Runs
                notificationInteractor.toggleNotificationForContact(contact)
            }

            Then("Тогда  происходит успешное удаление напоминания") {
                notificationRepository.cancelAlarm(
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                )
            }
        }

        Scenario("Добавление напоминания для контакта родившегося 29 февраля") {
            Given("Текущий год - 1999(не високосный), следующий 2000(високосный) 2 марта") {
                val day2 = 2
                currentCalendar.set(year1999, Calendar.MARCH, day2)
            }

            And("Есть контакт Иван с датой рождения 29 февраля") {
                birthday.set(year1988, Calendar.FEBRUARY, day29, 0, 0, 0)

                contact = Contact(
                        contactId,
                        URI.create(""),
                        ContactInfo(
                                contactName,
                                "",
                                "",
                                "",
                                ""),
                        birthday,
                        "")
            }

            And("И напоминание для этого контакта отсутствует") {
                every { notificationRepository.alarmIsUp(
                        contactId,
                        textNotification + contactName,
                        flagNoCreate
                ) } returns false
            }

            nextBirthday.set(year2000, Calendar.FEBRUARY, day29, 0, 0, 0)

            val birthdayCalendar = BirthdayCalendar(
                    nextBirthday[Calendar.YEAR],
                    nextBirthday[Calendar.MONTH],
                    nextBirthday[Calendar.DATE],
                    nextBirthday[Calendar.HOUR],
                    nextBirthday[Calendar.MINUTE],
                    nextBirthday[Calendar.SECOND]
            )

            When("Когда переключается напоминание для контатка Иван") {
                every { notificationRepository.setAlarm(
                        birthdayCalendar,
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                ) } just Runs

                notificationInteractor.toggleNotificationForContact(contact)
            }

            Then("Тогда  происходит успешное добавление напоминания на 2000 год 29 февраля") {
                verify { notificationRepository.setAlarm(
                        birthdayCalendar,
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                ) }
            }
        }

        Scenario("Добавление напоминания для контакта родившегося 29 февраля в високосный год") {
            Given("Текущий год - 2000(високосный) 1 марта") {
                val day1 = 1
                currentCalendar.set(year2000, Calendar.MARCH, day1)
            }

            And("Есть контакт Иван с датой рождения 29 февраля") {
                birthday.set(year1988, Calendar.FEBRUARY, day29, 0, 0, 0)

                contact = Contact(
                        contactId,
                        URI.create(""),
                        ContactInfo(
                                contactName,
                                "",
                                "",
                                "",
                                ""),
                        birthday,
                        "")
            }

            And("И напоминание для этого контакта отсутствует") {
                every { notificationRepository.alarmIsUp(
                        contactId,
                        textNotification + contactName,
                        flagNoCreate
                ) } returns false
            }

            val year2004 = 2004
            nextBirthday.set(year2004, Calendar.FEBRUARY, day29, 0, 0, 0)

            val birthdayCalendar = BirthdayCalendar(
                    nextBirthday[Calendar.YEAR],
                    nextBirthday[Calendar.MONTH],
                    nextBirthday[Calendar.DATE],
                    nextBirthday[Calendar.HOUR],
                    nextBirthday[Calendar.MINUTE],
                    nextBirthday[Calendar.SECOND]
            )

            When("Когда переключается напоминание для контатка Иван") {
                every { notificationRepository.setAlarm(
                        birthdayCalendar,
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                ) } just Runs

                notificationInteractor.toggleNotificationForContact(contact)
            }

            Then("Тогда  происходит успешное добавление напоминания на 2004 год 29 февраля") {
                verify { notificationRepository.setAlarm(
                        birthdayCalendar,
                        contactId,
                        textNotification + contactName,
                        flagUpdateCurrent
                ) }
            }
        }
    }
})