package com.example.eworkout.training.util

import com.example.eworkout.training.model.Instruction

class StringUlti {
    companion object{
        fun subInstructionsIntoList(originalString: String): MutableList<Instruction>
        {
            val result = mutableListOf<Instruction>()
            var subOriginalString = originalString

            var i = 1
            while(subOriginalString.contains("step " + i + ":", true))
            {
                if(subOriginalString.contains("step " + (i+1) + ":", true))
                {
                    i += 1
                    val stepI = subOriginalString.substringBefore("Step $i:")
                    val title = stepI.substringBefore(".")
                    val content = stepI.substringAfter(".")
                    result.add(Instruction(title, content))

                    subOriginalString = subOriginalString.substringAfter(stepI)
                }
                else
                {
                    val stepI = subOriginalString
                    val title = stepI.substringBefore(".")
                    val content = stepI.substringAfter(".")
                    result.add(Instruction(title, content))
                    i += 1
                }

            }
            return result
        }

        fun removeRepsPostfix(originalString: String): String
        {
            return originalString.removeSuffix("s")
        }
    }
}