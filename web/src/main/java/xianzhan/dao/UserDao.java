package xianzhan.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import xianzhan.pojo.User;

@Repository
@CacheConfig(cacheNames = {"user"})
public interface UserDao {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    // 缓存
    @Cacheable(key = "#p0")
    User selectByPrimaryKey(Integer id);

    // 删除缓存
    @CacheEvict(key = "#p0.id")
    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}