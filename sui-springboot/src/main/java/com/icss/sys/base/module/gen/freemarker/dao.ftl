package ${packageName}.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${packageName}.entity.${className};
import org.springframework.stereotype.Repository;

/**
* 【${comments}】数据接口
* ${className}Dao与${className}DaoMapper.xml进行接口映射
* 用于扩展自定义sql方法
*/
@Repository
public interface ${className}Dao extends BaseMapper<${className}> {

}

