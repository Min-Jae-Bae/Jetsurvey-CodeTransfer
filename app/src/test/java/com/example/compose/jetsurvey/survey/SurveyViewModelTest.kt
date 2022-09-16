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


/*컴퓨터야 나는 지금 이 앱 기능에 대한 테스트 코드를 작성하고 싶어.
일단은 나는 지금 기기나 에뮬레이터에서 실행할 JUnit4 테스트 클래스를 만들고 싶어
그래서 내가 만들고 싶은 클래스 앞에 @RunWith(AndroidJUnit4.class) 주석을 붙었어 그리고 설문 조사 뷰의 데이터와
관련해서 테스트 객체(Object)를 만들고 싶어. 그 제품의 이름은 SurveyViewModelTest 라고 이름을 지을거고,
이 제품의 설계도(Class)를 만들어 볼게
 */
@RunWith(AndroidJUnit4::class)
class SurveyViewModelTest {

    /*일단은 viewModel 이라는 변수를 만들건데 타입이 SurveyViewModel 클래스야 그리고 SurveyViewModelTest 에서만
     사용 가능해 그 이유는 private 이라는 접근 지정자를 사용했기 때문이야 그리고 이 변수는 lateinit var을 써서 이름만
      올려놓고 실제로 사용할 때 값을 넣을려고 만들었어 즉 늦은 초기화를 한다는 거지 ,변수 생성은 미리 해두고 초기화는
      해당 변수가 필요할 때만 초기화 할꺼야*/
    private lateinit var viewModel: SurveyViewModel

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
