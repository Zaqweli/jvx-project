package com.wildcodeschool.disco_project.repository;

import com.wildcodeschool.disco_project.DAO.AlbumDao;
import com.wildcodeschool.disco_project.config.AppConfig;
import com.wildcodeschool.disco_project.entity.Album;
import com.wildcodeschool.disco_project.util.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AlbumRepository implements AlbumDao<Album> {

    private final Logger L = LoggerFactory.getLogger(getClass());

    @Autowired
    private AppConfig config;

    public List<Album> findAllAlbums() {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtils.getConnection(config.mysql);
            statement = connection.prepareStatement(
                    "SELECT album.id, album.title, artist.id, artist.artist_name, album.year, label.name " +
                            "FROM album JOIN artist ON artist.id = album.artist_id " +
                            "JOIN label ON label.id = album.label_id " +
                            "ORDER BY album.title;"
            );
            resultSet = statement.executeQuery();

            List<Album> albums = new ArrayList<>();

            while (resultSet.next()) {

                Long id = resultSet.getLong("album.id");
                String title = resultSet.getString("album.title");
                Long year = resultSet.getLong("album.year");
                String label = resultSet.getString("label.name");
                String artistName = resultSet.getString("artist.artist_name");
                Long idArtist = resultSet.getLong("artist.id");
                albums.add(new Album(id, title, year, label, artistName, idArtist));
            }
            return albums;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }
        return null;
    }

    public List<Album> findAllAlbumsByIdArtist(Long artist_id) {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtils.getConnection(config.mysql);
            statement = connection.prepareStatement(
                    "SELECT " +
                            "album.id, " +
                            "album.title, " +
                            "album.year, " +
                            "label.name , " +
                            "artist.artist_name, " +
                            "artist.id " +
                            "FROM album JOIN label ON album.label_id = label.id " +
                            "JOIN artist ON artist.id = album.artist_id " +
                            "WHERE album.artist_id = ?;"

            );
            statement.setLong(1, artist_id);
            resultSet = statement.executeQuery();

            List<Album> albums = new ArrayList<>();

            while (resultSet.next()) {
                Long id_album = resultSet.getLong("album.id");
                String title = resultSet.getString("album.title");
                Long year = resultSet.getLong("album.year");
                String label = resultSet.getString("label.name");
                String artistName  = resultSet.getString("artist.artist_name");

                albums.add(new Album(id_album, title, year, label, artistName, artist_id));
            }
            return albums;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }
        return null;
    }
}
