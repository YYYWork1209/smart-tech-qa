package com.ai.springai.tools;

import com.ai.springai.entity.po.Course;
import com.ai.springai.entity.po.CourseReservation;
import com.ai.springai.entity.po.School;
import com.ai.springai.entity.query.CourseQuery;
import com.ai.springai.service.ICourseReservationService;
import com.ai.springai.service.ICourseService;
import com.ai.springai.service.ISchoolService;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class CourseTools {

    private final ISchoolService schoolService;
    private final ICourseService courseService;
    private final ICourseReservationService courseReservationService;

    @Tool(description = "依据条件查询课程")
    public List<Course> findCourse(@ToolParam(description = "课程查询条件", required = false)CourseQuery courseQuery){

        /**
         * 根据字段名获取对应的SFunction
         * @param fieldName 字段名
         * @return 对应的SFunction
         *
         */
        Function<String,SFunction<Course,?>> getColumn = fieldName -> {
            return switch (fieldName) {
                case "type" -> Course::getType;
                case "edu" -> Course::getEdu;
                default -> Course::getId;
            };
        };


        if(courseQuery == null){
            return List.of();
        }

        // 先构建查询条件
        LambdaQueryChainWrapper<Course> lambdaQueryChainWrapper = courseService.lambdaQuery().eq(courseQuery.getType() != null, Course::getType, courseQuery.getType())
                .le(courseQuery.getEdu() != null, Course::getEdu, courseQuery.getEdu());

        if (courseQuery.getSorts() != null){
            for (CourseQuery.Sort sort : courseQuery.getSorts()) {
                lambdaQueryChainWrapper.orderBy(true, sort.getAsc(),getColumn.apply(sort.getField()));
            }
        }

        return lambdaQueryChainWrapper.list();
    }

    @Tool(description = "查询所有校区")
    public List<School> queryAllSchools() {
        return schoolService.list();
    }

    @Tool(description = "生成课程预约单,并返回生成的预约单号")
    public String generateCourseReservation(
            String courseName, String studentName, String contactInfo, String school, String remark) {
        CourseReservation courseReservation = new CourseReservation();
        courseReservation.setCourse(courseName);
        courseReservation.setStudentName(studentName);
        courseReservation.setContactInfo(contactInfo);
        courseReservation.setSchool(school);
        courseReservation.setRemark(remark);
        courseReservationService.save(courseReservation);
        return String.valueOf(courseReservation.getId());
    }
}

