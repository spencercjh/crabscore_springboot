package top.spencercjh.crabscore.refactory.service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.*;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 所有涉及到文件上传的业务逻辑
 * <p>
 * TODO 存储文件和通过文件生成URL可以解耦,将IO操作解耦转异步,提升性能
 *
 * @author Spencer
 * @date 12/11/2019
 */
public interface BaseUploadFileService {
    String FILE_PATH_STRING_SEPARATOR = ",";
    char WINDOWS_FILE_SEPARATOR = '\\';
    char URL_SEPARATOR = '/';
    String PLUS = "+";
    String ENCODED_SPACE = "%20";
    Logger logger = LoggerFactory.getLogger(BaseUploadFileService.class);

    /**
     * 获取不带点 . 的文件名
     *
     * @param fileName fileName
     * @return fileName
     */
    @NotNull
    static String getFileName(@NotNull String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    /**
     * 获取不带点 .  的文件类型
     *
     * @param fileName fileName
     * @return fileType
     */
    @NotNull
    static String getFileType(@NotNull String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * 将以{@link #FILE_PATH_STRING_SEPARATOR}隔开的一组绝对路径转化为同样以它隔开的可以访问的一组URL路径字符串
     * <p>
     * 这个方法可能会返回空List，但不会返回空
     *
     * @param filePaths     以{@link #FILE_PATH_STRING_SEPARATOR}隔开的一组绝对路径
     * @param rootDirectory honeywell.file.root
     * @return 可达的静态资源URL
     */
    default List<String> parsePathsToUrls(final String filePaths, final String rootDirectory) {
        if (StringUtils.isBlank(filePaths)) {
            return Collections.emptyList();
        }
        // 懒加载
        String host = getHost();
        if (host == null) {
            logger.error("在非Web环境下获取不到主机名:{}", filePaths);
            return Arrays.asList(filePaths.split(FILE_PATH_STRING_SEPARATOR));
        }
        logger.info("获取到主机名:{}", host);
        final String[] paths = filePaths.split(FILE_PATH_STRING_SEPARATOR);
        final List<String> pathList = new ArrayList<>();
        for (final String fileAbsolutePath : paths) {
            String url = parsePathToUrl(fileAbsolutePath, rootDirectory);
            pathList.add(url);
        }
        return pathList;
    }

    /**
     * 获取本服务Host
     *
     * @return host
     */
    @Nullable
    private String getHost() {
        try {
            return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        } catch (IllegalStateException e) {
            logger.error(e.getMessage(), e.getCause());
            logger.error("在非Web环境下获取不到主机名");
            return null;
        }
    }

    /**
     * 绝对路径转化为可以访问的URL路径
     * <p>
     * 这个方法不会返回空
     *
     * @param fileAbsolutePath 绝对路径
     * @param rootDirectory    honeywell.file.root
     * @return 可达的静态资源URL
     */
    default String parsePathToUrl(final String fileAbsolutePath, final String rootDirectory) {
        String host = getHost();
        if (host == null) {
            logger.error("在非Web环境下获取不到主机名:{}", fileAbsolutePath);
            return fileAbsolutePath;
        }
        // 最后一个文件系统分隔符的位置，即它后面就是文件名
        final int lastSeparatorIndexInWindows = fileAbsolutePath.lastIndexOf(WINDOWS_FILE_SEPARATOR);
        final int lastSeparatorIndexInUnix = fileAbsolutePath.lastIndexOf(URL_SEPARATOR);
        if (lastSeparatorIndexInWindows == -1 && lastSeparatorIndexInUnix == -1) {
            logger.error("非法路径:{}", fileAbsolutePath);
            return fileAbsolutePath;
        }
        // URL前缀:协议头://主机名:端口/模块名称
        String prefix;
        // 有时候会出现 在Windows环境下获取到了Linux路径的问题，虽然拿到了URL也访问不了那个文件，但为了让URL能正常生成，故增加那么多if-else代码
        if (lastSeparatorIndexInWindows != -1) {
            // Windows的需要将\换成/
            prefix = host + fileAbsolutePath.substring(rootDirectory.length(), lastSeparatorIndexInWindows)
                    .replace(WINDOWS_FILE_SEPARATOR, URL_SEPARATOR);
        } else {
            prefix = host + fileAbsolutePath.substring(rootDirectory.length(), lastSeparatorIndexInUnix);
        }
        // 不是所有字符都能到URL里面去的，要符合RFC标准，所以要encode
        try {
            String actualFileName;
            if (lastSeparatorIndexInWindows != -1) {
                actualFileName = fileAbsolutePath.substring(lastSeparatorIndexInWindows + 1);
            } else {
                actualFileName = fileAbsolutePath.substring(lastSeparatorIndexInUnix + 1);
            }
            String needEncoded = URLEncoder.encode(actualFileName, StandardCharsets.UTF_8.name());
            /* 空格在URL里必须是%20 但上面这个URLEncoder是以application/x-www-form-urlencoded为标准进行encoding的，
            在这个标准下空格会被encode成+ 加号。（在query查询中的确空格都是这样操作的，参考各大搜索引擎）
            不符合我们的要求，直接GET会404.
            这是一个特例，所以要单独拿出来处理
            详情请参考：https://stackoverflow.com/questions/2t_record678551/when-to-encode-space-to-plus-or-20*/
            needEncoded = needEncoded.replace(PLUS, ENCODED_SPACE);
            return prefix + URL_SEPARATOR + needEncoded;
        } catch (UnsupportedEncodingException ignore) {
            // 注意：由于使用了StandardCharset 这个异常是不可能触发的!
            return "";
        }
    }

    /**
     * 存储单个文件
     *
     * @param multipartFile 需要存储的文件
     * @param directory     存储位置（绝对路径），且必须是配置文件中的{@code honeywell.file.root}下的子目录或本身
     * @param option        可选的参数，如果需要在{@link #setupFilePath(String, String, String...)}外使用，请override本方法，
     *                      不然请override {@code getFilePath}
     * @return filePath 这个文件在服务器上的绝对路径
     */
    default String saveFile(final MultipartFile multipartFile, final String directory, final String... option) {
        if (multipartFile == null) {
            return null;
        }
        final String filePath = setupFilePath(multipartFile.getOriginalFilename(), directory, option);
        try (final InputStream in = multipartFile.getInputStream()) {
            // 默认情况使用NIO Channel存储，部分特殊情况比如Junit时需要使用普通IO
            if (in instanceof FileInputStream) {
                return saveFileByNIO(filePath, (FileInputStream) in);
            } else {
                return saveFileByBIO(filePath, in);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Nullable
    private String saveFileByNIO(String filePath, FileInputStream in) {
        try (
                final FileChannel inputFileChannel = in.getChannel();
                final FileOutputStream os = new FileOutputStream(filePath);
                final FileChannel outputFileChannel = os.getChannel()
        ) {
            inputFileChannel.transferTo(0, inputFileChannel.size(), outputFileChannel);
            return filePath;
        } catch (IOException e) {
            return null;
        }
    }

    @TestOnly
    @Nullable
    private String saveFileByBIO(String filePath, InputStream in) {
        try (final OutputStream outputStream = new FileOutputStream(new File(filePath))) {
            IOUtils.copy(in, outputStream);
            return filePath;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 根据传入如的原始文件名，业务文件根目录和可选参数，拼一个文件路径
     * <p>
     * 其中最后真正的文件夹由{@link #setupActualDirectory(String, String, String...)}负责实现，这个方法需要在上层service层override
     * <p>
     * 其中最后真正的文件名由{@link #setupActualFileName(String, String, String...)}负责实现，这个方法需要在上层service层override
     * <p>
     * 一个文件路径就由这2部分构成，本方法负责将它们组合起来，所以这个方法不应该被override，所以它是final方法
     * <p>
     * 详细的使用例子请查看{@link com.honeywell.qrg.service.impl.ProductServiceImpl}和{@link com.honeywell.qrv.service.impl.VerificationServiceImpl}
     *
     * @param originalFilename      the original filename
     * @param businessRootDirectory 业务文件根目录。如果目录不存在会创建
     * @param option                可选的参数，默认实现不使用，请override相关方法以使用这个option
     * @return 拼接好的绝对路径
     * @throws RuntimeException 文件名不能为空
     */
    @SuppressWarnings("JavadocReference")
    default String setupFilePath(final String originalFilename, final String businessRootDirectory, String... option) throws RuntimeException {
        if (StringUtils.isBlank(originalFilename)) {
            throw new IllegalArgumentException("文件原始名不能为空");
        }
        String actualDirectory = setupActualDirectory(originalFilename, businessRootDirectory, option);
        actualDirectory = createDirectory(actualDirectory);
        return setupActualFileName(originalFilename, actualDirectory, option);
    }

    /**
     * 创建和业务逻辑有关的文件目录，默认实现是业务文件根目录本身，可以用可选参数创建带有各种业务信息的文件夹
     *
     * @param originalFilename      the original filename
     * @param businessRootDirectory 业务文件根目录。如果目录不存在会创建
     * @param option                可选参数
     * @return 文件夹路径，末尾不要带File.separator
     */
    @SuppressWarnings("unused")
    default String setupActualDirectory(String originalFilename, String businessRootDirectory, String... option) {
        return businessRootDirectory;
    }

    /**
     * 创建和业务逻辑有关的文件绝对路径，默认实现是目录+本身名字，可以用可选参数创建带有各种业务信息的文件路径
     *
     * @param originalFilename the original filename
     * @param directory        实际的文件所在目录，可能是默认的，也可能是override后带自己业务逻辑的
     * @param option           可选参数
     * @return 文件最终的绝对路径
     */
    @SuppressWarnings("unused")
    default String setupActualFileName(String originalFilename, String directory, String... option) {
        return directory + File.separator + originalFilename;
    }

    /**
     * 根据传来的文件夹路径，创建文件夹，如果失败的话返回应用的classPath。由于创建文件夹的逻辑是固定不变的，所以这个方法不能被override
     *
     * @param directory 目录路径
     * @return 目录路径
     */
    default String createDirectory(String directory) {
        boolean isError = false;
        if (StringUtils.isBlank(directory)) {
            String errorMessage = "目录名不能为空";
            logger.error(errorMessage);
            isError = true;
        } else {
            Path directoryPath = Paths.get(directory);
            // 目录不存在，创建目录
            if (!Files.exists(directoryPath)) {
                try {
                    Files.createDirectories(directoryPath);
                } catch (IOException e) {
                    logger.error("目录创建失败:{}", e.getMessage());
                    isError = true;
                }
            }
            // path存在，但不是文件夹
            if (!isError && !Files.isDirectory(directoryPath)) {
                logger.error("产品照片目录创建失败");
                isError = true;
            }
        }
        return isError ?
                Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() :
                directory;
    }

    /**
     * 遍历文件数组，分别调用{@link #saveFile(MultipartFile, String, String...)}方法，存储到{@link #setupFilePath(String, String, String...)}
     *
     * @param pictures  MultipartFiles
     * @param directory 存储的根目录
     * @param option    可选的参数。如果需要在{@link #setupFilePath(String, String, String...)}外使用，请override本方法，不然请override {@code getFilePath}
     * @return 用 FILE_PATH_STRING__SEPARATOR 分隔开的文件绝对路径
     * @throws RuntimeException 存储失败
     */
    default String saveFiles(MultipartFile[] pictures, String directory, String... option) throws RuntimeException {
        if (pictures == null || pictures.length == 0) {
            return null;
        }
        int countSuccess = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < pictures.length; i++) {
            String path = saveFile(pictures[i], directory, option);
            if (StringUtils.isNotBlank(path)) {
                if (countSuccess != 0) {
                    stringBuilder.append(FILE_PATH_STRING_SEPARATOR);
                }
                stringBuilder.append(path);
                countSuccess++;
            } else {
                logger.error("第{}个文件存储失败", i);
            }
        }
        if (countSuccess != pictures.length) {
            String errorMessage = "文件存储失败,expect: " + pictures.length + " ,actual: " + countSuccess;
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        return stringBuilder.toString();
    }
}
