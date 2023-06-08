package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, new RowMapper<Mpa>() {
            @Override
            public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
                return MpaDbStorage.mapRowToMpa(rs, rowNum);
            }
        });
    }

    @Override
    public Mpa findById(Integer id) {
        String sql = "SELECT * FROM mpa WHERE mpa_id = ?";
        return (Mpa) jdbcTemplate.query(sql, new Object[]{id}, new RowMapper<Mpa>() {
                    @Override
                    public Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                        return MpaDbStorage.mapRowToMpa(resultSet, rowNum);
                    }
                })
                .stream()
                .findAny()
                .orElse(null);
    }

    static Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa();
        mpa.setId(resultSet.getInt("mpa_id"));
        mpa.setName(resultSet.getString("mpa_name"));
        return mpa;
    }
}
