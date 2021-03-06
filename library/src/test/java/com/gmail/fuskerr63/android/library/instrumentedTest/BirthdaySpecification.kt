package com.gmail.fuskerr63.android.library.instrumentedTest

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import com.gmail.fuskerr63.android.library.constants.CONTACT_ID
import com.gmail.fuskerr63.android.library.constants.CONTACT_NAME
import com.gmail.fuskerr63.android.library.constants.DAY_1
import com.gmail.fuskerr63.android.library.constants.DAY_2
import com.gmail.fuskerr63.android.library.constants.DAY_29
import com.gmail.fuskerr63.android.library.constants.DAY_7
import com.gmail.fuskerr63.android.library.constants.DAY_8
import com.gmail.fuskerr63.android.library.constants.DAY_9
import com.gmail.fuskerr63.android.library.constants.TEXT_NOTIFICATION
import com.gmail.fuskerr63.android.library.constants.YEAR_1988
import com.gmail.fuskerr63.android.library.constants.YEAR_1999
import com.gmail.fuskerr63.android.library.constants.YEAR_2000
import com.gmail.fuskerr63.android.library.constants.YEAR_2004
import com.gmail.fuskerr63.android.library.viewmodel.ContactViewModel
import com.gmail.fuskerr63.java.entity.BirthdayCalendar
import com.gmail.fuskerr63.java.entity.Contact
import com.gmail.fuskerr63.java.entity.ContactInfo
import com.gmail.fuskerr63.java.interactor.ContactModel
import com.gmail.fuskerr63.java.interactor.DatabaseModel
import com.gmail.fuskerr63.java.interactor.NotificationInteractorImpl
import com.gmail.fuskerr63.java.interactor.NotificationRepository
import com.gmail.fuskerr63.java.interactor.NotificationTime
import com.gmail.fuskerr63.java.repository.ContactDetailsRepository
import com.gmail.fuskerr63.java.repository.ContactListRepository
import com.gmail.fuskerr63.java.repository.LocationRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import java.net.URI
import java.util.Calendar
import java.util.GregorianCalendar

class BirthdaySpecification : Spek({
    val notificationTime = mockk<NotificationTime>()
    val notificationRepository = mockk<NotificationRepository>(relaxed = true)
    val contactListRepository = mockk<ContactListRepository>()
    val contactDetailsRepository = mockk<ContactDetailsRepository>()
    val locationRepository = mockk<LocationRepository>()
    val viewModelDispatcherProvider = ViewModelDispatcherTest()

    val birthday = GregorianCalendar()
    val currentCalendar = GregorianCalendar()
    val nextBirthday = GregorianCalendar()

    lateinit var birthdayCalendar: BirthdayCalendar
    lateinit var contactViewModel: ContactViewModel

    var contact = Contact(
        CONTACT_ID,
        URI.create(""),
        ContactInfo(
            CONTACT_NAME,
            "",
            "",
            "",
            ""
        ),
        birthday,
        ""
    )

    fun init() {
        val contactInteractor = ContactModel(
            contactListRepository,
            contactDetailsRepository
        )
        val databaseModel = DatabaseModel(locationRepository)
        val notificationInteractor = NotificationInteractorImpl(
            notificationTime,
            notificationRepository,
            TEXT_NOTIFICATION
        )

        contactViewModel = ContactViewModel(
            id = CONTACT_ID,
            contactInteractor = contactInteractor,
            databaseInteractor = databaseModel,
            notificationInteractor = notificationInteractor,
            viewModelDispatcher = viewModelDispatcherProvider
        )
    }

    fun setupCurrentCalendar(year: Int, month: Int, day: Int) {
        currentCalendar.set(year, month, day, 0, 0, 0)
    }

    fun setupBirthdayAndContact(year: Int, month: Int, day: Int) {
        birthday.set(year, month, day, 0, 0, 0)
        contact = Contact(
            CONTACT_ID,
            URI.create(""),
            ContactInfo(
                CONTACT_NAME,
                "",
                "",
                "",
                ""
            ),
            birthday,
            ""
        )
    }

    fun setupNextBirthday(year: Int, month: Int, day: Int) {
        nextBirthday.set(year, month, day, 0, 0, 0)
        birthdayCalendar = BirthdayCalendar(
            nextBirthday[Calendar.YEAR],
            nextBirthday[Calendar.MONTH],
            nextBirthday[Calendar.DATE],
            nextBirthday[Calendar.HOUR],
            nextBirthday[Calendar.MINUTE],
            nextBirthday[Calendar.SECOND]
        )
    }

    fun setupMockAlarmIsUp(answer: Boolean) {
        every {
            notificationRepository.alarmIsUp(
                CONTACT_ID,
                TEXT_NOTIFICATION + CONTACT_NAME
            )
        } returns answer
    }

    beforeEachTest {
        ArchTaskExecutor.getInstance().setDelegate(object : TaskExecutor() {
            override fun executeOnDiskIO(runnable: Runnable) {
                runnable.run()
            }

            override fun isMainThread(): Boolean {
                return true
            }

            override fun postToMainThread(runnable: Runnable) {
                runnable.run()
            }
        })
        init()
    }

    afterEachTest { ArchTaskExecutor.getInstance().setDelegate(null) }

    Feature("Я как пользователь хочу устанавливать напоминание о дне рождения контакта") {
        every { contactDetailsRepository.getContactById(CONTACT_ID) } returns flowOf(contact)
        every { locationRepository.getFlowUserById(CONTACT_ID) } returns flowOf(null)
        every { notificationTime.currentTimeCalendar } returns currentCalendar

        Scenario("Успешное добавление напоминания, День Рождения в текущем году был") {
            Given("Текущий год - $YEAR_1999(не високосный) $DAY_9 сентября") {
                setupCurrentCalendar(YEAR_1999, Calendar.SEPTEMBER, DAY_9)
            }

            And("Есть контакт $CONTACT_NAME с датой рождения $DAY_8 сентября") {
                setupBirthdayAndContact(YEAR_1988, Calendar.SEPTEMBER, DAY_8)
            }

            And("И напоминание для этого контакта отсутствует") {
                setupMockAlarmIsUp(false)
                setupNextBirthday(YEAR_2000, Calendar.SEPTEMBER, DAY_8)
            }

            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта $CONTACT_NAME") {
                contactViewModel.onClickBirthday()
            }

            Then("Тогда происходит успешное добавление напоминания на $YEAR_2000 год $DAY_8 сентября") {
                verify {
                    notificationRepository.setAlarm(
                        birthdayCalendar,
                        CONTACT_ID,
                        TEXT_NOTIFICATION + CONTACT_NAME
                    )
                }
            }
        }

        Scenario("Успешное добавление напоминания, Дня Рождения еще в текущем году не было") {
            Given("Текущий год - $YEAR_1999(не високосный) $DAY_7 сентября") {
                setupCurrentCalendar(YEAR_1999, Calendar.SEPTEMBER, DAY_7)
            }

            And("Есть контакт $CONTACT_NAME с датой рождения $DAY_8 сентября") {
                setupBirthdayAndContact(YEAR_1988, Calendar.SEPTEMBER, DAY_8)
            }

            And("И напоминание для этого контакта отсутствует") {
                setupMockAlarmIsUp(false)
                setupNextBirthday(YEAR_1999, Calendar.SEPTEMBER, DAY_8)
            }

            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта $CONTACT_NAME") {
                contactViewModel.onClickBirthday()
            }

            Then("Тогда происходит успешное добавление напоминания на $YEAR_1999 год $DAY_8 сентября") {
                verify {
                    notificationRepository.setAlarm(
                        birthdayCalendar,
                        CONTACT_ID,
                        TEXT_NOTIFICATION + CONTACT_NAME
                    )
                }
            }
        }

        Scenario("Успешное удаление напоминания") {
            Given("Текущий год - $YEAR_1999(не високосный)") {
                setupCurrentCalendar(YEAR_1999, Calendar.SEPTEMBER, DAY_9)
            }

            And("Есть контакт $CONTACT_NAME с датой рождения $DAY_8 сентября") {
                setupBirthdayAndContact(YEAR_1988, Calendar.SEPTEMBER, DAY_8)
            }

            And("И для него включено напоминание на $YEAR_2000 год $DAY_8 сентября") {
                setupMockAlarmIsUp(true)
            }

            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта $CONTACT_NAME") {
                contactViewModel.onClickBirthday()
            }

            Then("Тогда происходит успешное удаление напоминания") {
                verify {
                    notificationRepository.cancelAlarm(
                        CONTACT_ID,
                        TEXT_NOTIFICATION + CONTACT_NAME
                    )
                }
            }
        }

        Scenario("Добавление напоминания для контакта родившегося $DAY_29 февраля") {
            Given("Текущий год - $YEAR_1999(не високосный), следующий $YEAR_2000(високосный) $DAY_2 марта") {
                setupCurrentCalendar(YEAR_1999, Calendar.MARCH, DAY_2)
            }

            And("Есть контакт $CONTACT_NAME с датой рождения $DAY_29 февраля") {
                setupBirthdayAndContact(YEAR_1988, Calendar.FEBRUARY, DAY_29)
            }

            And("И напоминание для этого контакта отсутствует") {
                setupMockAlarmIsUp(false)
                setupNextBirthday(YEAR_2000, Calendar.FEBRUARY, DAY_29)
            }

            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта $CONTACT_NAME") {
                contactViewModel.onClickBirthday()
            }

            Then("Тогда происходит успешное добавление напоминания на $YEAR_2000 год $DAY_29 февраля") {
                verify {
                    notificationRepository.setAlarm(
                        birthdayCalendar,
                        CONTACT_ID,
                        TEXT_NOTIFICATION + CONTACT_NAME
                    )
                }
            }
        }

        Scenario("Добавление напоминания для контакта родившегося $DAY_29 февраля в високосный год") {
            Given("Текущий год - $YEAR_2000(високосный) $DAY_1 марта") {
                setupCurrentCalendar(YEAR_2000, Calendar.MARCH, DAY_1)
            }

            And("Есть контакт $CONTACT_NAME с датой рождения $DAY_29 февраля") {
                setupBirthdayAndContact(YEAR_1988, Calendar.FEBRUARY, DAY_29)
            }

            And("И напоминание для этого контакта отсутствует") {
                setupMockAlarmIsUp(false)
                setupNextBirthday(YEAR_2004, Calendar.FEBRUARY, DAY_29)
            }

            When("Когда пользователь кликает на кнопку напоминания в детальной информации контакта $CONTACT_NAME") {
                contactViewModel.onClickBirthday()
            }

            Then("Тогда происходит успешное добавление напоминания на $YEAR_2004 год $DAY_29 февраля") {
                verify {
                    notificationRepository.setAlarm(
                        birthdayCalendar,
                        CONTACT_ID,
                        TEXT_NOTIFICATION + CONTACT_NAME
                    )
                }
            }
        }
    }
})
