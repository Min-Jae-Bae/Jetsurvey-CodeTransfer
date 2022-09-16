/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.jetsurvey.survey

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/*
컴퓨터야 나는 지금 이 앱 기능에 대한 테스트 코드를 작성하고 싶어. 일단은 나는 지금 기기나 에뮬레이터에서 실행할 JUnit4
테스트 클래스를 만들고 싶어 그래서 내가 만들고 싶은 클래스 앞에 @RunWith(AndroidJUnit4.class) 주석을 붙였지
그리고 테스트 객체(Object)를 만들었어. 그 제품의 이름은 SurveyViewModelTest 라고 이름을 만들었어,
이 제품의 설계도(Class)를 이제 만들어 볼게
 */

@RunWith(AndroidJUnit4::class)
class SurveyViewModelTest {

    /*
    일단은 viewModel 이라는 변수를 만들고 타입이 SurveyViewModel 클래스로 지정했어 SurveyViewModelTest 에서만
     사용 가능해 그 이유는 private 이라는 접근 지정자를 사용했기 때문이야. 이 변수는 lateinit var을 써서 이름만
     올려놓고 실제로 사용할 때 값을 넣을려고 만들었어 즉 변수 생성은 미리 해두고 초기화는 해당 변수가 필요할 때만 초기화 할꺼야
     */

    private lateinit var viewModel: SurveyViewModel

    /*
    일단은 @Before이라는 주석을 사용해서 테스트틑 시작하기 전에 기능을 불러올거야 기능의 이름은 setUp(설정)이라고 지었어
    기능 안에서는 viewModel을 불러올 건데 viewModel은 SurveyViewModel 클래스와 같아 그리고 SurveyViewModel
    클래스는 2개의 생성자가 존재해 첫번째 생성자는 TestSurveyRepository를 호출했어. 조사 저장소를 불러오는 것을
    테스트하는 기능이지
    두번째는 PhotoUriManager를 호출했어 .이미지 주소를 관리하는 기능이지. 테스트 안에서 현재
    어플리케이션의 문맥이나 글을 검색하는 기능을 제공하는 ApplicationProvider을 사용했고 그 안에 있는
    어플리케이션 Context를 얻기 위해서 getApplicatioContext 메소드를 호출했지
    */

    /*
    Provides ability to retrieve the current application Context in tests.
    테스트에서 현재 애플리케이션 Context를 검색하는 기능을 제공한다
    This can be useful if you need to access the application assets
    앱 정보에 접근할 필요가 있다면 ApplicationProvider(This)는 유용할 수 있다

    접근 하는 방법은 밑에 3가지가 있다
    getApplicationContext().getAssets())
    preferences (eg getApplicationContext().getSharedPreferences()),
    file system (eg getApplicationContext().getDir())
    one of the many other context APIs in test
    */

    @Before
    fun setUp() {
        viewModel = SurveyViewModel(
            TestSurveyRepository(),
            PhotoUriManager(ApplicationProvider.getApplicationContext())
        )
    }

    @Test
    fun onDatePicked_storesValueCorrectly() {
        // Select a date
        val initialDateMilliseconds = 0L
        viewModel.onDatePicked(dateQuestionId, initialDateMilliseconds)

        // Get the stored date
        val newDateMilliseconds = viewModel.getCurrentDate(dateQuestionId)

        // Verify they're identical
        assertThat(newDateMilliseconds).isEqualTo(initialDateMilliseconds)
    }
}

const val dateQuestionId = 1

class TestSurveyRepository : SurveyRepository {

    private val testSurvey = Survey(
        title = -1,
        questions = listOf(
            Question(
                id = dateQuestionId,
                questionText = -1,
                answer = PossibleAnswer.Action(label = -1, SurveyActionType.PICK_DATE)
            )
        )
    )

    override fun getSurvey() = testSurvey

    override fun getSurveyResult(answers: List<Answer<*>>): SurveyResult {
        TODO("Not yet implemented")
    }
}
