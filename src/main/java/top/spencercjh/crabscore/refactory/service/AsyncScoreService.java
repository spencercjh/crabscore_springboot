package top.spencercjh.crabscore.refactory.service;

import org.jetbrains.annotations.NotNull;
import top.spencercjh.crabscore.refactory.model.Crab;

import java.io.Serializable;

/**
 * The interface Async score service.
 *
 * @author Spencer
 * @date 2020 /1/31
 */
public interface AsyncScoreService {
    /**
     * Async save scores by crab.
     *
     * @param crab the crab;
     */
    void asyncSaveScoresByCrab(@NotNull Crab crab);

    /**
     * Async delete scores by crab.
     *
     * @param crabId the crab id;
     */
    void asyncDeleteScoresByCrab(@NotNull Serializable crabId);
}
