package tager.imagemaster.service;

import tager.imagemaster.entity.util.Result;
import tager.imagemaster.entity.util.ResultMessage;

public interface AdminService {
    ResultMessage login(String key);

    Result getStatistics();
}