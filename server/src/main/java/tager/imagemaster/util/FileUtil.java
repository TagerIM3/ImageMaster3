package tager.imagemaster.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import tager.imagemaster.entity.util.ResultMessage;

import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
public class FileUtil {
    @Value("${upload-path}")
    private String IMAGE_ROOT_DIR;

    private final String[] IMAGE_SUFFIX = {"jpg", "png", "jpeg"};

    private final int BUFFER = 2048;

    private List<String> imagePaths = new ArrayList<>();

    private String imagePath;

    public ResultMessage saveTaskImages(int requestorId, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getSize() <= 0)
            return ResultMessage.FILE_DAMAGE;

        try {
            ZipInputStream zipInputStream;
            zipInputStream = new ZipInputStream(multipartFile.getInputStream(), Charset.forName("gbk"));

            ZipEntry zipEntry;

            String subDir = File.separator + requestorId + File.separator + LocalDateTime.now().toString() + File.separator;

            File file = new File(IMAGE_ROOT_DIR + subDir);

            if (!file.exists())
                file.mkdirs();

            imagePaths.clear();
            int num = 0;


            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String zipEntryName = zipEntry.getName();

                if (zipEntryName.contains(File.separator))
                    continue;

                String[] split = zipEntryName.split("\\.");

                if (split.length < 2)
                    continue;

                String suffix = split[split.length - 1];

                if (Arrays.asList(IMAGE_SUFFIX).contains(suffix.toLowerCase())) {
                    int count;
                    byte[] data = new byte[BUFFER];

                    String imageName = UUID.randomUUID().toString().replace("-", "") + "." + suffix;

                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(IMAGE_ROOT_DIR + subDir + imageName), BUFFER);

                    while ((count = zipInputStream.read(data, 0, BUFFER)) != -1)
                        bufferedOutputStream.write(data, 0, count);

                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();

                    num++;
                    imagePaths.add(subDir + imageName);
                }
            }

            zipInputStream.close();

            if (num != 0)
                return ResultMessage.SUCCESS;

            file.delete();
            return ResultMessage.NO_IMAGE;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMessage.FILE_DAMAGE;
        }
    }

    public ResultMessage saveAvatar(int userId, MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.getSize() <= 0)
            return ResultMessage.FILE_DAMAGE;


        try {
            FileInputStream fileOutputStream = (FileInputStream) multipartFile.getInputStream();

            String subDir = File.separator + userId + File.separator;
            File file = new File(IMAGE_ROOT_DIR + subDir);

            if (!file.exists())
                file.mkdirs();

            imagePath = null;

            int count;
            byte[] data = new byte[BUFFER];

            String[] split = multipartFile.getOriginalFilename().split("\\.");

            String imageName = UUID.randomUUID().toString().replace("-", "") + "." + split[split.length - 1];

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(IMAGE_ROOT_DIR + subDir + imageName), BUFFER);
            while ((count = fileOutputStream.read(data, 0, BUFFER)) != -1)
                bufferedOutputStream.write(data, 0, count);

            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            imagePath = subDir + imageName;

            return ResultMessage.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultMessage.FILE_DAMAGE;
        }
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public String getImagePath() {
        return imagePath;
    }
}
