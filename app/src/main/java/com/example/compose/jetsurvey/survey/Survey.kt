/*
 * Copyright 2020 The Android Open Source Project
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

import android.net.Uri
import androidx.annotation.StringRes

data class SurveyResult(
    val library: String,
    @StringRes val result: Int,
    @StringRes val description: Int
)

/*
   @StringRes
   Denotes that an integer parameter, field or method return value is expected to be a String resource reference
   정수형 매개변수, 내부 또는 매소드 반환 값은 문자열 정보로 예상되는 것을 표시함
   */

data class Survey(
    @StringRes val title: Int,
    val questions: List<Question>
)

/* data로 만들어진 Question 제품은
     값 지정시 변경 불가인 저장공간 id를 만들고 정수형만 받는다

     받은 값을 문자열 정보로 예상된다는 표시를 @StringRes로 해주고 값 지정시 변경 불가인 저장공간 questionText(질문 글)를
     만들고 정수형만 받게 한다 (-1 값을 넣음).
     값 지정지 변경 불가인 저장공간 answer를 만들고 PossibleAnswer

    받은 값을 문자열 정보로 예상된다는 표시를 @StringRes로 해주고 값 지정시 변경 불가인 저장공간 description(설명)
     만들고 정수형 값을 받을 수 있다. 그런데 안에 값이 아무것도 없을 수 있다는 것을 가능하게 해주는( ? )를
     사용한다. 즉 아무것도 값을 넣지 않아도 에러가 나지 않는다.

     깂을 지정시 변경이 불가능한(val) 저장공간 permissionRequired(허락요청)을 만들거야. 이 저장공간에는 List<String>을
     써서 여러 값이 차례대로 들어갈 수 있게 목록을 만들었고 그 모든 값들은 다 문자열이라고 지정했어. 현재는 emptyLust()를 사용해서
     아무것도 없는 빈 목록이라고 만들었어

     받은 값을 문자열 정보로 예상된다는 표시를 @StringRes로 표시했고 값 지정시 변경 불가인 저장공간 permissionsRationaleText
     (허가 근거 글)을 만들었어. 이 저장소에는 정수형(Int)과 값이 없는 null을 받을 수 있어. 값이 없는 null을 받을 수 있는
     이유는 (?)를 사용했기 때문이야 즉 permissionRationaleText 저장공간이 값이 없을 수가 있다라는 것 입니다.
     */

    // ?은 null 존재 가능성이 있다. !!은 null 존재 가능성이 없다.

data class Question(
    val id: Int,
    @StringRes val questionText: Int,
    val answer: PossibleAnswer,
    @StringRes val description: Int? = null,
    val permissionsRequired: List<String> = emptyList(),
    @StringRes val permissionsRationaleText: Int? = null
)

/**
 * Type of supported actions for a survey
 */
/*enum 클래스는 상태 모드 등 유사한 값들을 고유값으로 만들어 사용하기 위해 사용*/
enum class SurveyActionType { PICK_DATE, TAKE_PHOTO, SELECT_CONTACT }

sealed class SurveyActionResult {
    data class Date(val dateMillis: Long) : SurveyActionResult()
    data class Photo(val uri: Uri) : SurveyActionResult()
    data class Contact(val contact: String) : SurveyActionResult()
}

/*
sealed class란 ?
sealed 클래스가 자기 자신이 추상 클래스이고, 자신을 상속받는 여러 서브 클래스를 가진다.
sealed 클래스의 서브 클래스들은 반드시 같은 파일 내에 선언되어야 함으로써 다른 곳에서
자신의 서브 클래스가 없다는 것을 알려주는 것과 같다.
*/
sealed class PossibleAnswer {
    data class SingleChoice(val optionsStringRes: List<Int>) : PossibleAnswer()
    data class SingleChoiceIcon(val optionsStringIconRes: List<Pair<Int, Int>>) : PossibleAnswer()
    data class MultipleChoice(val optionsStringRes: List<Int>) : PossibleAnswer()
    data class MultipleChoiceIcon(val optionsStringIconRes: List<Pair<Int, Int>>) : PossibleAnswer()
    data class Action(
        @StringRes val label: Int,
        val actionType: SurveyActionType
    ) : PossibleAnswer()

    data class Slider(
        val range: ClosedFloatingPointRange<Float>,
        val steps: Int,
        @StringRes val startText: Int,
        @StringRes val endText: Int,
        @StringRes val neutralText: Int,
        val defaultValue: Float = 5.5f
    ) : PossibleAnswer()
}

sealed class Answer<T : PossibleAnswer> {
    object PermissionsDenied : Answer<Nothing>()
    data class SingleChoice(@StringRes val answer: Int) : Answer<PossibleAnswer.SingleChoice>()
    data class MultipleChoice(val answersStringRes: Set<Int>) :
        Answer<PossibleAnswer.MultipleChoice>()

    data class Action(val result: SurveyActionResult) : Answer<PossibleAnswer.Action>()
    data class Slider(val answerValue: Float) : Answer<PossibleAnswer.Slider>()
}

/**
 * Add or remove an answer from the list of selected answers depending on whether the answer was
 * selected or deselected.
 */
fun Answer.MultipleChoice.withAnswerSelected(
    @StringRes answer: Int,
    selected: Boolean
): Answer.MultipleChoice {
    val newStringRes = answersStringRes.toMutableSet()
    if (!selected) {
        newStringRes.remove(answer)
    } else {
        newStringRes.add(answer)
    }
    return Answer.MultipleChoice(newStringRes)
}
