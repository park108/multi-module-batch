package com.skt.nova.batch.common.item;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

public class NovaItemReaderBuilder<T> {

    public enum ReaderType {
        JDBC_CURSOR,
        JDBC_PAGING,
        JPA_CURSOR,
        JPA_PAGING,
        REPOSITORY
    }

    private ReaderType type;
    private int fetchSize;
    private DataSource dataSource;
    private EntityManagerFactory emf;
    private JpaRepository<T, ?> repository;
    private String sql;
    private PreparedStatementSetter preparedStatementSetter;
    private String jpql;
    private RowMapper<T> rowMapper;
    private Class<T> entityClass;

    public NovaItemReaderBuilder<T> readerType(ReaderType type) {
        this.type = type;
        return this;
    }

    public NovaItemReaderBuilder<T> fetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    public NovaItemReaderBuilder<T> dataSource(DataSource ds) {
        this.dataSource = ds;
        return this;
    }

    public NovaItemReaderBuilder<T> entityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
        return this;
    }

    public NovaItemReaderBuilder<T> repository(JpaRepository<T, ?> repo) {
        this.repository = repo;
        return this;
    }

    public NovaItemReaderBuilder<T> sql(String sql) {
        this.sql = sql;
        return this;
    }

    public NovaItemReaderBuilder<T> preparedStatementSetter(PreparedStatementSetter setter) {
        this.preparedStatementSetter = setter;
        return this;
    }

    public NovaItemReaderBuilder<T> jpql(String jpql) {
        this.jpql = jpql;
        return this;
    }

    public NovaItemReaderBuilder<T> rowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
        return this;
    }

    public NovaItemReaderBuilder<T> entityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
        return this;
    }

    public NovaItemReader<T> build() {
        ItemStreamReader<T> reader;

        switch (type) {
            case JDBC_CURSOR:
                // JdbcCursorItemReader는 내부적으로 ResultSet을 커서로 유지하며 read()에서 .next()로 이동합니다.
                // 👉 ResultSet은 스레드 간 공유되면 안 됨
                // 👉 SyncTaskExecutor는 같은 스레드 내에서 순차적으로 read → process → write가 실행되므로 안전합니다.
                JdbcCursorItemReader<T> cursorReader = new JdbcCursorItemReader<>();
                cursorReader.setDataSource(dataSource);
                cursorReader.setFetchSize(fetchSize);
                cursorReader.setSql(sql);
                if(preparedStatementSetter != null) {
                    cursorReader.setPreparedStatementSetter(preparedStatementSetter);
                }
                cursorReader.setRowMapper(rowMapper);
                try {
                    cursorReader.afterPropertiesSet();
                } catch (Exception e) {
                    throw new IllegalStateException("JdbcCursorItemReader 초기화 실패", e);
                }
                reader = cursorReader;
                break;
            case JDBC_PAGING:
                JdbcPagingItemReader<T> pagingReader = new JdbcPagingItemReader<>();
                pagingReader.setDataSource(dataSource);
                pagingReader.setRowMapper(rowMapper);
                try {
                    pagingReader.afterPropertiesSet();
                } catch (Exception e) {
                    throw new IllegalStateException("JdbcPagingItemReader 초기화 실패", e);
                }
                // set query provider 생략
                reader = pagingReader;
                break;
            case JPA_CURSOR:
                JpaCursorItemReader<T> jpaCursorReader = new JpaCursorItemReader<>();
                jpaCursorReader.setEntityManagerFactory(emf);
                jpaCursorReader.setQueryString(jpql);
                try {
                    jpaCursorReader.afterPropertiesSet();
                } catch (Exception e) {
                    throw new IllegalStateException("JpaCursorItemReader 초기화 실패", e);
                }
                reader = jpaCursorReader;
                break;
            case JPA_PAGING:
                JpaPagingItemReader<T> jpaPagingReader = new JpaPagingItemReader<>();
                jpaPagingReader.setEntityManagerFactory(emf);
                jpaPagingReader.setQueryString(jpql);
                try {
                    jpaPagingReader.afterPropertiesSet();
                } catch (Exception e) {
                    throw new IllegalStateException("JpaPagingItemReader 초기화 실패", e);
                }
                reader = jpaPagingReader;
                break;
            case REPOSITORY:
                RepositoryItemReader<T> repositoryReader = new RepositoryItemReader<>();
                repositoryReader.setRepository(repository);
                repositoryReader.setMethodName("findAll");
                try {
                    repositoryReader.afterPropertiesSet();
                } catch (Exception e) {
                    throw new IllegalStateException("RepositoryItemReader 초기화 실패", e);
                }
                reader = repositoryReader;
                break;
            default:
                throw new IllegalArgumentException("Unknown reader type");
        }

        // Facade wrapping
        return new NovaItemReaderImpl<>(reader);
    }
}
